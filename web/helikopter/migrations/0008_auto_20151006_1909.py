# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('helikopter', '0007_auto_20151006_1905'),
    ]

    operations = [
        migrations.AlterField(
            model_name='history',
            name='event',
            field=models.IntegerField(choices=[(0, b'Added'), (1, b'Removed'), (2, b'Modified')]),
        ),
    ]
