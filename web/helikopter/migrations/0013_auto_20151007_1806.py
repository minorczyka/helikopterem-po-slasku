# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('helikopter', '0012_auto_20151007_1803'),
    ]

    operations = [
        migrations.RenameField(
            model_name='androidversion',
            old_name='file',
            new_name='file_path',
        ),
    ]
