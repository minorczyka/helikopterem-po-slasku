import os
from django.shortcuts import render
from django.utils.safestring import mark_safe
from django.utils.encoding import smart_str
from django.contrib.auth.decorators import login_required, user_passes_test
from django.http import HttpResponseRedirect, HttpResponse
from django.core.servers.basehttp import FileWrapper
from datetime import datetime, date
from calendar import month_name

from helikopter.classes import RideCalendar
from helikopter.models import Ride, Person, Passenger, History, AndroidVersion
from helikopter.forms import RideForm, UserProfileRegistrationForm, UserProfileEditForm, AndroidVersionForm


def home(request):
    return render(request, 'helikopter/home.html', {'version_code': AndroidVersion.getActualVersion()})


def register(request):
    registered = False
    if request.method == 'POST':
        register_form = UserProfileRegistrationForm(data=request.POST)
        if register_form.is_valid():
            register_form.save()
            registered = True
        else:
            print register_form.errors
    else:
        register_form = UserProfileRegistrationForm()
    return render(request, 'helikopter/register.html', {'register_form': register_form, 'registered': registered})


@login_required
def edit_profile(request):
    if request.method == 'POST':
        profile_form = UserProfileEditForm(request.user.person, data=request.POST)
        if profile_form.is_valid():
            profile_form.save()
        else:
            print profile_form.errors
    else:
        initial = {'name': request.user.person.name, 'email': request.user.email, 'color': request.user.person.color}
        profile_form = UserProfileEditForm(request.user.person, initial=initial)
    return render(request, 'helikopter/profile.html', {'profile_form': profile_form})


@login_required
def default_calendar(request):
    now = datetime.now()
    return HttpResponseRedirect('/helikopter/calendar/' + str(now.year) + "/" + str(now.month))


@login_required
def calendar(request, year, month):
    year = int(year)
    month = int(month)
    party = request.user.person.party

    my_rides = Ride.objects.order_by('date').filter(
        driver__party=party, date__year=year, date__month=month
    )
    cal = RideCalendar(my_rides).formatmonth(year, month)

    # lCalendarFromMonth = datetime(year, month, 1)
    # lCalendarToMonth = datetime(year, month, monthrange(year, month)[1])
    # lContestEvents = ContestEvent.objects.filter(date_of_event__gte=lCalendarFromMonth, date_of_event__lte=lCalendarToMonth)
    previousYear = year
    previousMonth = month - 1
    if previousMonth == 0:
        previousMonth = 12
        previousYear = year - 1
    nextYear = year
    nextMonth = month + 1
    if nextMonth == 13:
        nextMonth = 1
        nextYear = year + 1
    yearAfterThis = year + 1
    yearBeforeThis = year - 1
    return render(request, 'helikopter/calendar.html', {'calendar': mark_safe(cal),
                                                        'Month': month,
                                                        'MonthName': month_name[month],
                                                        'Year': year,
                                                        'PreviousMonth': previousMonth,
                                                        'PreviousMonthName': month_name[previousMonth],
                                                        'PreviousYear': previousYear,
                                                        'NextMonth': nextMonth,
                                                        'NextMonthName': month_name[nextMonth],
                                                        'NextYear': nextYear,
                                                        'YearBeforeThis': yearBeforeThis,
                                                        'YearAfterThis': yearAfterThis, })


@login_required
def day_details(request, year, month, day):
    year = int(year)
    month = int(month)
    day = int(day)
    party = request.user.person.party
    rides = Ride.objects.filter(driver__party=party, date__year=year, date__month=month, date__day=day)
    request.session['last_year'] = year
    request.session['last_month'] = month
    request.session['last_day'] = day
    return render(request, 'helikopter/day.html', {'day': day, 'month': month, 'year': year, 'rides': rides})


def saveRide(ride_form, person, ride):
    if ride_form.is_valid():
        ride_date = ride_form.cleaned_data['date']
        driver = Person.objects.get(pk=ride_form.cleaned_data['driver'])
        if ride is None:
            ride = Ride(date=ride_date, driver=driver)
            History.addHistory(person, ride.date, History.ADDED_TYPE)
        else:
            ride.date = ride_date
            ride.driver = driver
            ride.passengers.all().delete()
            History.addHistory(person, ride.date, History.MODIFIED_TYPE)
        ride.save()

        for it in person.party.members.all():
            ammount = int(ride_form.cleaned_data[it.user.username])
            if ammount > 0:
                if ammount == 2:
                    twice = True
                else:
                    twice = False
                passenger = Passenger(ride=ride, person=it, twice=twice)
                passenger.save()
        return HttpResponseRedirect('/helikopter/calendar/' + str(ride_date.year) + "/" + str(ride_date.month) + "/" + str(ride_date.day))
    else:
        # TODO moze cos innego?
        return HttpResponseRedirect('/helikopter/')


@login_required
def new_ride(request):
    party = request.user.person.party
    if request.method == 'POST':
        ride_form = RideForm(party, data=request.POST)
        return saveRide(ride_form, request.user.person, None)
    else:
        if request.session.get('last_year', 0) != 0:
            year = request.session.get('last_year', 0)
            month = request.session.get('last_month', 0)
            day = request.session.get('last_day', 0)
            ride_date = date(year, month, day)
        else:
            ride_date = date.today()
        ride_form = RideForm(party, initial={'date': ride_date})
        return render(request, 'helikopter/ride.html', {'ride_form': ride_form, 'update_id': 0})


@login_required
def ride(request, ride_id):
    party = request.user.person.party
    ride = Ride.objects.get(pk=ride_id)
    if request.method == 'POST':
        ride_form = RideForm(party, data=request.POST)
        return saveRide(ride_form, request.user.person, ride)
    else:
        initial = {'date': ride.date, 'driver': ride.driver.id}
        for it in ride.passengers.all():
            if it.twice:
                initial[it.person.user.username] = 2
            else:
                initial[it.person.user.username] = 1
        ride_form = RideForm(party, initial=initial)
        return render(request, 'helikopter/ride.html', {'ride_form': ride_form, 'update_id': ride_id})


@login_required
def delete_ride(request, ride_id):
    party = request.user.person.party
    ride = Ride.objects.get(pk=ride_id)
    if ride.driver.party.id == party.id:
        ride.delete()
        History.addHistory(request.user.person, ride.date, History.REMOVED_TYPE)
        return HttpResponseRedirect('/helikopter/calendar/' + str(ride.date.year) + "/" + str(ride.date.month) + "/" + str(ride.date.day))
    else:
        return HttpResponseRedirect('/helikopter/calendar/')


@login_required
def person(request, person_id):
    person = Person.objects.get(pk=person_id)
    count = Ride.objects.filter(driver_id=person_id).count()
    summary = []
    for it in person.party.members.all():
        summary.append({'name': it.name, 'points': person.computePoints(it)})
    return render(request, 'helikopter/person.html', {'person': person, 'count': count, 'summary': summary})


@login_required
@user_passes_test(lambda u: u.is_superuser)
def add_android_version(request):
    if request.method == 'POST':
        form = AndroidVersionForm(request.POST, request.FILES)
        if form.is_valid():
            name = form.cleaned_data['version_name']
            code = form.cleaned_data['version_code']
            new_apk = AndroidVersion(apk_file=request.FILES['apk_file'], version_name=name, version_code=code)
            new_apk.save()
            return HttpResponseRedirect('/helikopter/download/')
        else:
            return HttpResponseRedirect('/helikopter/')
    else:
        form = AndroidVersionForm()
        return render(request, 'helikopter/download_add.html', {'form': form})


def download_android_version(request, file_id):
    try:
        androidFile = AndroidVersion.objects.get(version_code=file_id)
        filename = androidFile.apk_file.path
        wrapper = FileWrapper(file(filename))

        response = HttpResponse(wrapper, content_type='application/force-download')
        response['Content-Disposition'] = 'attachment; filename=%s' % smart_str('helikopter.apk')
        response['Content-Type'] = ''
        response['Content-Length'] = os.path.getsize(filename)
        return response
    except:
        return HttpResponseRedirect('/helikopter/download/')
