# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('helikopter', '0009_androidversion_androidversionfeature'),
    ]

    operations = [
        migrations.AlterField(
            model_name='androidversion',
            name='versionCode',
            field=models.IntegerField(unique=True),
        ),
    ]
