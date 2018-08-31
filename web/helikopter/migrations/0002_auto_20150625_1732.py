# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('helikopter', '0001_initial'),
    ]

    operations = [
        migrations.AlterField(
            model_name='passenger',
            name='person',
            field=models.ForeignKey(related_name=b'passengers', to='helikopter.Person'),
        ),
        migrations.AlterField(
            model_name='passenger',
            name='ride',
            field=models.ForeignKey(related_name=b'passengers', to='helikopter.Ride'),
        ),
        migrations.AlterField(
            model_name='person',
            name='party',
            field=models.ForeignKey(related_name=b'members', blank=True, to='helikopter.Party', null=True),
        ),
        migrations.AlterField(
            model_name='ride',
            name='driver',
            field=models.ForeignKey(related_name=b'rides', to='helikopter.Person'),
        ),
    ]
