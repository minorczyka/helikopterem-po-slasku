from helikopter.models import Person, Ride, Passenger, UserColor, History
from rest_framework import serializers


class UserColorSerializer(serializers.ModelSerializer):
    class Meta:
        model = UserColor
        fields = ('name', 'backgroundColor', 'textColor')


class PersonSerializer(serializers.ModelSerializer):
    color = UserColorSerializer(read_only=True)
    username = serializers.SerializerMethodField('getNick')

    class Meta:
        model = Person
        fields = ('id', 'name', 'username', 'color')

    def getNick(self, obj):
        return obj.user.username


class PassengerSerializer(serializers.ModelSerializer):
    class Meta:
        model = Passenger
        fields = ('person', 'twice')


class RideSerializer(serializers.ModelSerializer):
    passengers = PassengerSerializer(many=True, read_only=True)

    class Meta:
        model = Ride
        fields = ('id', 'driver', 'date', 'passengers')


class HistorySerializer(serializers.ModelSerializer):
    class Meta:
        model = History
        fields = ('history_date', 'related_date', 'person', 'event')
