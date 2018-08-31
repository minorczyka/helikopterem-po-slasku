# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('helikopter', '0006_auto_20151006_1846'),
    ]

    operations = [
        migrations.RenameField(
            model_name='history',
            old_name='user',
            new_name='person',
        ),
    ]
