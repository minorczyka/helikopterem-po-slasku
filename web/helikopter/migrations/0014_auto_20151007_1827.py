# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('helikopter', '0013_auto_20151007_1806'),
    ]

    operations = [
        migrations.AlterField(
            model_name='androidversion',
            name='file_path',
            field=models.FileField(upload_to=b'apk'),
        ),
    ]
