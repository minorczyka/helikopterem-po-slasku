from django.conf.urls import patterns, url
from helikopter_api import views

urlpatterns = patterns('',
                       url(r'^people/$', views.PersonList.as_view()),
                       url(r'^rides/(?P<year>[0-9]{4})/(?P<month>[0-9]+)$', views.RideList.as_view()),
                       url(r'^rides/(?P<year>[0-9]{4})/(?P<month>[0-9]+)/(?P<day>[0-9]+)$', views.RideList.as_view()),
                       url(r'^stats/$', views.Stats.as_view()),
                       url(r'^stats/(?P<person_id>[0-9]+)$', views.Stats.as_view()),
                       url(r'^history/$', views.HistoryList.as_view()),
                       url(r'^version/$', views.AndroidVersionCode.as_view()),
                       )
