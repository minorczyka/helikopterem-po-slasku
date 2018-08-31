# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('helikopter', '0005_history'),
    ]

    operations = [
        migrations.AddField(
            model_name='history',
            name='history_date',
            field=models.DateTimeField(default=None, auto_now_add=True),
            preserve_default=False,
        ),
        migrations.AddField(
            model_name='history',
            name='related_date',
            field=models.DateField(default=None),
            preserve_default=False,
        ),
    ]
