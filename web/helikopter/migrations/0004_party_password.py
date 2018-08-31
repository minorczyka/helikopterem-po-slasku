# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('helikopter', '0003_auto_20150924_1626'),
    ]

    operations = [
        migrations.AddField(
            model_name='party',
            name='password',
            field=models.CharField(default='qwe123', max_length=30),
            preserve_default=False,
        ),
    ]
