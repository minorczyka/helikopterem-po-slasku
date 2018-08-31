from calendar import HTMLCalendar
from datetime import date
from itertools import groupby
from django.utils.html import conditional_escape


class RideCalendar(HTMLCalendar):

    def __init__(self, rides):
        super(RideCalendar, self).__init__()
        self.rides = self.group_by_day(rides)

    def formatday(self, day, weekday):
        if day != 0:
            cssclass = self.cssclasses[weekday]
            if date.today() == date(self.year, self.month, day):
                cssclass += ' today'
            body = ['<ul>']
            body.append('<a href="{0}">'.format(day))
            body.append("Show details")
            body.append('</a>')
            if day in self.rides:
                cssclass += ' filled'
                for ride in self.rides[day]:
                    body.append('<li>')
                    # body.append('<a href="calendar/{0}/{1}/{2}">'.format(ride.date.year, ride.date.month, ride.date.day))
                    body.append('<a href="../../../ride/{0}">'.format(ride.id))
                    body.append(conditional_escape(ride.driver.name))
                    body.append('</a></li>')
                body.append('</ul>')
            return self.day_cell(cssclass, '%d %s' % (day, ''.join(body)))
        return self.day_cell('noday', '&nbsp;')

    def formatmonth(self, year, month):
        self.year, self.month = year, month
        return super(RideCalendar, self).formatmonth(year, month)

    def group_by_day(self, rides):
        field = lambda ride: ride.date.day
        return dict(
            [(day, list(items)) for day, items in groupby(rides, field)]
        )

    def day_cell(self, cssclass, body):
        return '<td class="%s">%s</td>' % (cssclass, body)
