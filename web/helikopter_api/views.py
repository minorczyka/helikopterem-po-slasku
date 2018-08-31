from django.core.urlresolvers import reverse
from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework import status
from rest_framework.generics import ListAPIView

from helikopter.models import Ride, Person, History, AndroidVersion
from helikopter_api.classes import RidePostSerializer
from helikopter_api.serializers import PersonSerializer, RideSerializer, HistorySerializer


class PersonList(APIView):
    def get(self, request):
        party = request.user.person.party
        people = Person.objects.filter(party=party).order_by('name')
        serializer = PersonSerializer(people, many=True)
        return Response(serializer.data)


class RideList(APIView):
    def get(self, request, year, month, day=None):
        party = request.user.person.party
        if day is None:
            rides = Ride.objects.filter(driver__party=party, date__year=year, date__month=month)
        else:
            rides = Ride.objects.filter(driver__party=party, date__year=year, date__month=month, date__day=day)
        serializer = RideSerializer(rides, many=True)
        return Response(serializer.data)

    def post(self, request, year, month, day):
        serializer = RidePostSerializer(request.data, request.user.person)
        serializer.save(year, month, day)
        return Response({'status': 'OK'}, status=status.HTTP_201_CREATED)


class Stats(APIView):
    def get(self, request, person_id=None):
        if person_id is None:
            result = []
            for it in request.user.person.party.members.all():
                result.append(it.statsPersonal())
        else:
            person = Person.objects.get(id=person_id)
            result = person.statsPersonal()
        return Response(result, status=status.HTTP_200_OK)


class HistoryList(ListAPIView):
    serializer_class = HistorySerializer

    def get_queryset(self):
        party = self.request.user.person.party
        return History.objects.filter(person__party=party).order_by('-history_date')


class AndroidVersionCode(APIView):
    def get(self, request):
        version_code = AndroidVersion.getActualVersion()
        link = reverse('download_file', args=[version_code])
        full_link = request.build_absolute_uri(link)
        return Response({'version_code': version_code, 'link': full_link}, status=status.HTTP_200_OK)
