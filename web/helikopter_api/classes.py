from helikopter.models import Ride, Person, Passenger, History
from datetime import date


class RidePostSerializer:

    def __init__(self, data, person):
        self.data = data
        self.person = person

    def validate(self, data, ride):
        ride.driver = Person.objects.get(id=data['driver'])
        ride.passengers.all().delete()
        for it in data['passengers']:
            passenger = Passenger()
            passenger.ride = ride
            passenger.person = Person.objects.get(id=it['person'])
            passenger.twice = it['twice']
            passenger.save()
        ride.save()

    def update(self, data):
        ride = Ride.objects.get(id=data['id'])
        self.validate(data, ride)

    def create(self, data):
        ride = Ride()
        ride.driver = Person.objects.get(id=data['driver'])
        ride.date = data['date']
        ride.save()
        self.validate(data, ride)

    def save(self, year, month, day):
        currentRideCount = len(Ride.objects.filter(date__year=year, date__month=month, date__day=day))
        rideDate = date(int(year), int(month), int(day))
        if len(self.data) == 0 and currentRideCount != 0:
            History.addHistory(self.person, rideDate, History.REMOVED_TYPE)
        elif currentRideCount == 0 and len(self.data) != 0:
            History.addHistory(self.person, rideDate, History.ADDED_TYPE)
        else:
            History.addHistory(self.person, rideDate, History.MODIFIED_TYPE)

        ride_ids = []
        for it in self.data:
            ride_ids.append(it['id'])
        for it in Ride.objects.filter(date__year=year, date__month=month, date__day=day):
            if it.id not in ride_ids:
                it.delete()

        for it in self.data:
            if len(Ride.objects.filter(id=it['id'])) == 1:
                self.update(it)
            else:
                self.create(it)
