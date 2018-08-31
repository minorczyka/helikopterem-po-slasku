# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('helikopter', '0011_auto_20151007_1752'),
    ]

    operations = [
        migrations.RemoveField(
            model_name='androidversion',
            name='file_name',
        ),
        migrations.AddField(
            model_name='androidversion',
            name='file',
            field=models.FileField(default=None, upload_to=b''),
            preserve_default=False,
        ),
    ]
