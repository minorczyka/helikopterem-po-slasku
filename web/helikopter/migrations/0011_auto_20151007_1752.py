# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('helikopter', '0010_auto_20151007_1743'),
    ]

    operations = [
        migrations.RenameField(
            model_name='androidversion',
            old_name='fileName',
            new_name='file_name',
        ),
        migrations.RenameField(
            model_name='androidversion',
            old_name='versionCode',
            new_name='version_code',
        ),
        migrations.RenameField(
            model_name='androidversion',
            old_name='versionName',
            new_name='version_name',
        ),
    ]
