from django import forms
from django.contrib.auth.models import User
from helikopter.models import Person, UserColor, Party

from crispy_forms.helper import FormHelper
from crispy_forms.layout import Submit
from crispy_forms.bootstrap import InlineRadios
from registration.forms import RegistrationForm

RIDE_CHOICES = (
    (0, 'None'),
    (1, 'Once'),
    (2, 'Twice'),
)


class RideForm(forms.Form):
    date = forms.DateField()

    def __init__(self, party, *args, **kwargs):
        super(RideForm, self).__init__(*args, **kwargs)

        first_id = party.members.order_by('name').first().id
        self.fields['driver'] = forms.ChoiceField(choices=[(it.id, it.name) for it in party.members.all().order_by('name')], initial=first_id)
        for person in party.members.all():
            self.fields[person.user.username] = forms.ChoiceField(label=person.name, choices=RIDE_CHOICES, initial=0)

        self.helper = FormHelper(self)
        self.helper.form_id = 'id-RideForm'
        self.helper.form_class = 'form-post'
        self.helper.form_method = 'post'
        self.helper.form_action = ''
        self.helper.add_input(Submit('submit', 'Submit'))

        self.helper[1] = InlineRadios('driver')
        i = 2
        for person in party.members.all().order_by('name'):
            self.helper[i] = InlineRadios(person.user.username)
            i = i + 1


def unusedColors(person):
    return UserColor.objects.exclude(users__contains=person.party.members.exclude(pk=person.pk))


class UserProfileEditForm(forms.Form):
    name = forms.CharField(
        label='Name',
        min_length=3,
        max_length=30,
        required=True,
        )

    email = forms.EmailField(
        label='Email',
        required=True,
        )

    def __init__(self, person, *args, **kwargs):
        super(UserProfileEditForm, self).__init__(*args, **kwargs)
        self.person = person
        self.helper = FormHelper()
        self.helper.form_id = 'id-editProfileForm'
        self.helper.form_class = 'form-post'
        self.helper.form_method = 'post'
        self.helper.form_action = 'submit_edit_profile'
        self.helper.add_input(Submit('submit', 'Submit'))

        self.fields['color'] = forms.ModelChoiceField(label='Color', queryset=unusedColors(person), empty_label=None, required=True)

    def is_valid(self):
        if super(UserProfileEditForm, self).is_valid():
            color = self.cleaned_data['color']
            if color in unusedColors(self.person):
                return True
            else:
                self.add_error('color', 'Color is already used')
                return False
        else:
            return False

    def save(self):
        self.person.name = self.cleaned_data['name']
        self.person.user.email = self.cleaned_data['email']
        self.person.color = self.cleaned_data['color']
        self.person.save()
        self.person.user.save()


class UserProfileRegistrationForm(RegistrationForm):
    name = forms.CharField(
        label='Name',
        min_length=3,
        max_length=30,
        required=True,
        )

    color = forms.ModelChoiceField(
        label='Color',
        queryset=UserColor.objects.all(),
        empty_label=None,
        required=True,
        )

    party = forms.ModelChoiceField(
        label='Party',
        queryset=Party.objects.all(),
        empty_label=None,
        required=True,
        )

    party_password = forms.CharField(
        label='Party password',
        widget=forms.PasswordInput,
        min_length=5,
        max_length=80,
        required=True,
        )

    def __init__(self, *args, **kwargs):
        super(UserProfileRegistrationForm, self).__init__(*args, **kwargs)
        self.helper = FormHelper()
        self.helper.form_id = 'id-registerForm'
        self.helper.form_class = 'form-post'
        self.helper.form_method = 'post'
        self.helper.form_action = 'submit_register'
        self.helper.add_input(Submit('submit', 'Submit'))

    def is_valid(self):
        if super(UserProfileRegistrationForm, self).is_valid():
            party = self.cleaned_data['party']
            if party.password == self.cleaned_data['party_password']:
                color = self.cleaned_data['color']
                unusedColors = UserColor.objects.exclude(users__contains=party.members.all())
                if color in unusedColors:
                    return True
                else:
                    self.add_error('color', 'Color is already used')
                    return False
            else:
                self.add_error('party_password', 'Wrong password')
                return False
        else:
            return False

    def save(self):
        super(UserProfileRegistrationForm, self).save()
        user = User.objects.get(username=self.cleaned_data['username'])
        person = Person()
        person.user = user
        person.name = self.cleaned_data['name']
        person.color = self.cleaned_data['color']
        person.party = self.cleaned_data['party']
        person.save()


class AndroidVersionForm(forms.Form):
    apk_file = forms.FileField(
        label='Select a .apk file',
        help_text='max. 42 megabytes'
    )

    version_name = forms.CharField(
        label='Version name',
        min_length=1,
        max_length=30,
        required=True,
        )

    version_code = forms.IntegerField(
        label='Version code',
        min_value=1,
        required=True,
        )

    def __init__(self, *args, **kwargs):
        super(AndroidVersionForm, self).__init__(*args, **kwargs)
        self.helper = FormHelper(self)
        self.helper.add_input(Submit('submit', 'Submit'))

    # def is_valid(self):
    #     if super(AndroidVersionForm, self).is_valid():
    #         print 'super valid'
    #         return True
    #         # party = self.cleaned_data['party']
    #         # if party.password == self.cleaned_data['party_password']:
    #         #     return True
    #         # else:
    #         #     self.add_error('party_password', 'Wrong password')
    #         #     return False
    #     else:
    #         print 'super invalid'
    #         return False
