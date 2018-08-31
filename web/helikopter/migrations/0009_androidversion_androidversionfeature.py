# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('helikopter', '0008_auto_20151006_1909'),
    ]

    operations = [
        migrations.CreateModel(
            name='AndroidVersion',
            fields=[
                ('id', models.AutoField(verbose_name='ID', serialize=False, auto_created=True, primary_key=True)),
                ('fileName', models.CharField(max_length=50)),
                ('versionName', models.CharField(max_length=30)),
                ('versionCode', models.IntegerField()),
            ],
            options={
            },
            bases=(models.Model,),
        ),
        migrations.CreateModel(
            name='AndroidVersionFeature',
            fields=[
                ('id', models.AutoField(verbose_name='ID', serialize=False, auto_created=True, primary_key=True)),
                ('text', models.CharField(max_length=256)),
                ('version', models.ForeignKey(related_name=b'features', to='helikopter.AndroidVersion')),
            ],
            options={
            },
            bases=(models.Model,),
        ),
    ]
