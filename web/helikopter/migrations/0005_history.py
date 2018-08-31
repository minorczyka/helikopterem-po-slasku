# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('helikopter', '0004_party_password'),
    ]

    operations = [
        migrations.CreateModel(
            name='History',
            fields=[
                ('id', models.AutoField(verbose_name='ID', serialize=False, auto_created=True, primary_key=True)),
                ('event', models.CharField(max_length=1, choices=[(b'0', b'Added'), (b'1', b'Removed'), (b'2', b'Modified')])),
                ('user', models.ForeignKey(related_name=b'histories', to='helikopter.Person')),
            ],
            options={
            },
            bases=(models.Model,),
        ),
    ]
