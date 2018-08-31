from django.conf.urls import patterns, url, include
from django.views.generic.list import ListView
from django.contrib.auth.decorators import login_required
from helikopter import views
from helikopter.models import History, AndroidVersion
from django.contrib.auth import views as auth_views

urlpatterns = patterns('',
                       url(r'^$', views.home, name='home'),
                       url(r'^accounts/profile/', views.edit_profile, name='edit_profile'),
                       url(r'^accounts/register/', views.register, name='register'),
                       url(r'^password/reset/$', auth_views.password_reset, name='password_reset'),
                       url(r'^accounts/password/reset/done/$', auth_views.password_reset_done, name='password_reset_done'),
                       url(r'^password/reset/complete/$', auth_views.password_reset_complete, name='password_reset_complete'),
                       url(r'^password/reset/confirm/(?P<uidb64>[0-9A-Za-z_\-]+)/(?P<token>.+)/$', auth_views.password_reset_confirm, name='password_reset_confirm'),
                       url(r'^accounts/', include('registration.backends.simple.urls')),
                       url(r'^calendar$', views.default_calendar, name='default_calendar'),
                       url(r'^calendar/(?P<year>[0-9]{4})/(?P<month>[0-9]+)/$', views.calendar, name='calendar'),
                       url(r'^calendar/(?P<year>[0-9]{4})/(?P<month>[0-9]+)/(?P<day>[0-9]+)$', views.day_details, name='day_details'),
                       url(r'^ride/$', views.new_ride, name='new_ride'),
                       url(r'^ride/(?P<ride_id>[0-9]+)$', views.ride, name='ride'),
                       url(r'^ride/delete/(?P<ride_id>[0-9]+)$', views.delete_ride, name='delete_ride'),
                       url(r'^person/(?P<person_id>[0-9]+)$', views.person, name='person'),
                       url(r'^history/$', login_required(ListView.as_view(queryset=History.objects.all().order_by('-history_date'), paginate_by=25)), name='history_list'),
                       url(r'^download/$', ListView.as_view(queryset=AndroidVersion.objects.all().order_by('-version_code'), template_name='helikopter/download_list.html', paginate_by=10), name='download_list'),
                       url(r'^download/(?P<file_id>[0-9]+)$', views.download_android_version, name='download_file'),
                       url(r'^download/add/$', views.add_android_version, name='download_add'),
                       )
