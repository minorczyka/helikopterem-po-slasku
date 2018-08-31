# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('helikopter', '0014_auto_20151007_1827'),
    ]

    operations = [
        migrations.RenameField(
            model_name='androidversion',
            old_name='file_path',
            new_name='apk_file',
        ),
    ]
