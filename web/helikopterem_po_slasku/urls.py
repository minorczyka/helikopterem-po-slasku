from django.conf.urls import patterns, include, url
from django.contrib import admin

urlpatterns = patterns('',
                       url(r'^$', include('helikopter.urls')),
                       url(r'^helikopter/', include('helikopter.urls')),
                       url(r'^api/', include('helikopter_api.urls')),
                       url(r'^admin/', include(admin.site.urls)),
                       )
