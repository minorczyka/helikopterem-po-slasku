# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('helikopter', '0002_auto_20150625_1732'),
    ]

    operations = [
        migrations.CreateModel(
            name='UserColor',
            fields=[
                ('id', models.AutoField(verbose_name='ID', serialize=False, auto_created=True, primary_key=True)),
                ('name', models.CharField(max_length=30)),
                ('backgroundColor', models.CharField(max_length=9)),
                ('textColor', models.CharField(max_length=9)),
            ],
            options={
            },
            bases=(models.Model,),
        ),
        migrations.RemoveField(
            model_name='person',
            name='color_b',
        ),
        migrations.RemoveField(
            model_name='person',
            name='color_g',
        ),
        migrations.RemoveField(
            model_name='person',
            name='color_r',
        ),
        migrations.AddField(
            model_name='person',
            name='color',
            field=models.ForeignKey(related_name=b'users', default=1, to='helikopter.UserColor'),
            preserve_default=False,
        ),
    ]
