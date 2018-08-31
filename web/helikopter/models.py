from django.db import models
from django.contrib.auth.models import User
from django.db.models import Max


class Party(models.Model):
    name = models.CharField(max_length=30)
    password = models.CharField(max_length=30)

    def __unicode__(self):
        return self.name


class UserColor(models.Model):
    name = models.CharField(max_length=30)
    backgroundColor = models.CharField(max_length=9)
    textColor = models.CharField(max_length=9)

    def __unicode__(self):
        return self.name


class Person(models.Model):
    user = models.OneToOneField(User)
    party = models.ForeignKey(Party, blank=True, null=True, related_name='members')
    color = models.ForeignKey(UserColor, related_name='users')

    name = models.CharField(max_length=30)
    has_car = models.BooleanField(default=False)

    def __unicode__(self):
        return self.user.username

    def computePoints(self, other):
        myRides = Passenger.objects.filter(person_id=other.id, ride__driver__id=self.id)
        otherRides = Passenger.objects.filter(person_id=self.id, ride__driver__id=other.id)
        points = 0
        for it in myRides:
            if it.twice:
                points = points + 2
            else:
                points = points + 1
        for it in otherRides:
            if it.twice:
                points = points - 2
            else:
                points = points - 1
        return points

    def statsSummary(self):
        result = []
        for it in self.party.members.all().order_by('name'):
            points = 0
            for ite in self.party.members.all():
                points = points + it.computePoints(ite)
            driver = self.rides.all().count()
            result.append({'id': it.id, 'driver': driver, 'points': points})
        return result

    def statsPersonal(self):
        result = []
        for person in self.party.members.all():
            result.append({'id': person.id, 'points': self.computePoints(person)})
        driver = self.rides.all().count()
        return {'id': self.id, 'driver': driver, 'points': result}


class Ride(models.Model):
    driver = models.ForeignKey(Person, related_name='rides')
    date = models.DateField()

    def __unicode__(self):
        return self.driver.name + ' ' + str(self.date)


class Passenger(models.Model):
    ride = models.ForeignKey(Ride, related_name='passengers')
    person = models.ForeignKey(Person, related_name='passengers')
    twice = models.BooleanField(default=False)

    def __unicode__(self):
        return self.person.name + ' (' + self.ride.driver.name + ' ' + str(self.ride.date) + ')'


class History(models.Model):
    ADDED_TYPE = 0
    REMOVED_TYPE = 1
    MODIFIED_TYPE = 2
    EVENT_TYPE_CHOICES = (
        (ADDED_TYPE, 'Added'),
        (REMOVED_TYPE, 'Removed'),
        (MODIFIED_TYPE, 'Modified'),
    )

    @staticmethod
    def addHistory(person, date, event):
        history = History()
        history.person = person
        history.related_date = date
        history.event = event
        history.save()

    history_date = models.DateTimeField(auto_now_add=True)
    related_date = models.DateField()
    person = models.ForeignKey(Person, related_name='histories')
    event = models.IntegerField(choices=EVENT_TYPE_CHOICES)

    def __unicode__(self):
        return self.person.user.username + ': ' + History.EVENT_TYPE_CHOICES[self.event][1]


class AndroidVersion(models.Model):
    apk_file = models.FileField(upload_to='apk')
    version_name = models.CharField(max_length=30)
    version_code = models.IntegerField(unique=True, null=False)

    def __unicode__(self):
        return self.version_name

    @staticmethod
    def getActualVersion():
        version = AndroidVersion.objects.all().aggregate(Max('version_code'))
        return version['version_code__max']


class AndroidVersionFeature(models.Model):
    text = models.CharField(max_length=256)
    version = models.ForeignKey(AndroidVersion, related_name='features')

    def __unicode__(self):
        return self.version.version_name + ': ' + self.text
