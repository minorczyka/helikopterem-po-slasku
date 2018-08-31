# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations
from django.conf import settings


class Migration(migrations.Migration):

    dependencies = [
        migrations.swappable_dependency(settings.AUTH_USER_MODEL),
    ]

    operations = [
        migrations.CreateModel(
            name='Party',
            fields=[
                ('id', models.AutoField(verbose_name='ID', serialize=False, auto_created=True, primary_key=True)),
                ('name', models.CharField(max_length=30)),
            ],
            options={
            },
            bases=(models.Model,),
        ),
        migrations.CreateModel(
            name='Passenger',
            fields=[
                ('id', models.AutoField(verbose_name='ID', serialize=False, auto_created=True, primary_key=True)),
                ('twice', models.BooleanField(default=False)),
            ],
            options={
            },
            bases=(models.Model,),
        ),
        migrations.CreateModel(
            name='Person',
            fields=[
                ('id', models.AutoField(verbose_name='ID', serialize=False, auto_created=True, primary_key=True)),
                ('name', models.CharField(max_length=30)),
                ('color_r', models.IntegerField(default=0)),
                ('color_g', models.IntegerField(default=0)),
                ('color_b', models.IntegerField(default=0)),
                ('has_car', models.BooleanField(default=False)),
                ('party', models.ForeignKey(blank=True, to='helikopter.Party', null=True)),
                ('user', models.OneToOneField(to=settings.AUTH_USER_MODEL)),
            ],
            options={
            },
            bases=(models.Model,),
        ),
        migrations.CreateModel(
            name='Ride',
            fields=[
                ('id', models.AutoField(verbose_name='ID', serialize=False, auto_created=True, primary_key=True)),
                ('date', models.DateField()),
                ('driver', models.ForeignKey(related_name=b'driver', to='helikopter.Person')),
            ],
            options={
            },
            bases=(models.Model,),
        ),
        migrations.AddField(
            model_name='passenger',
            name='person',
            field=models.ForeignKey(related_name=b'person', to='helikopter.Person'),
            preserve_default=True,
        ),
        migrations.AddField(
            model_name='passenger',
            name='ride',
            field=models.ForeignKey(related_name=b'ride', to='helikopter.Ride'),
            preserve_default=True,
        ),
    ]
