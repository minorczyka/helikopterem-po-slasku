"""
Django settings for helikopterem_po_slasku project.

For more information on this file, see
https://docs.djangoproject.com/en/1.7/topics/settings/

For the full list of settings and their values, see
https://docs.djangoproject.com/en/1.7/ref/settings/
"""

# Build paths inside the project like this: os.path.join(BASE_DIR, ...)
import os
BASE_DIR = os.path.dirname(os.path.dirname(__file__))


# Quick-start development settings - unsuitable for production
# See https://docs.djangoproject.com/en/1.7/howto/deployment/checklist/

# SECURITY WARNING: keep the secret key used in production secret!
SECRET_KEY = '0a0tw=xi-%2gu_&e((71z&%)qb7!pn+or6aig37_!o3&1j0kro'

# SECURITY WARNING: don't run with debug turned on in production!
DEBUG = True

TEMPLATE_DEBUG = True

ALLOWED_HOSTS = ['helikopter.pythonanywhere.com']


# Application definition

INSTALLED_APPS = (
    'django.contrib.admin',
    'django.contrib.auth',
    'django.contrib.contenttypes',
    'django.contrib.sessions',
    'django.contrib.messages',
    'django.contrib.staticfiles',
    'registration',
    'rest_framework',
    'helikopter',
    'helikopter_api',
    'crispy_forms',
)

MIDDLEWARE_CLASSES = (
    'django.contrib.sessions.middleware.SessionMiddleware',
    'django.middleware.common.CommonMiddleware',
    'django.middleware.csrf.CsrfViewMiddleware',
    'django.contrib.auth.middleware.AuthenticationMiddleware',
    'django.contrib.auth.middleware.SessionAuthenticationMiddleware',
    'django.contrib.messages.middleware.MessageMiddleware',
    'django.middleware.clickjacking.XFrameOptionsMiddleware',
)

ROOT_URLCONF = 'helikopterem_po_slasku.urls'

WSGI_APPLICATION = 'helikopterem_po_slasku.wsgi.application'


DATABASES = {
    'default': {
        'ENGINE': 'django.db.backends.sqlite3',
        'NAME': os.path.join(BASE_DIR, 'db.sqlite3'),
    }
}


LANGUAGE_CODE = 'en-us'

TIME_ZONE = 'Europe/Warsaw'

USE_I18N = True

USE_L10N = True

USE_TZ = False


STATIC_PATH = os.path.join(BASE_DIR, 'static')

STATIC_URL = '/static/'


MEDIA_ROOT = os.path.join(BASE_DIR, 'media')

MEDIA_URL = '/media/'

STATICFILES_DIRS = (
    STATIC_PATH,
)

TEMPLATE_PATH = os.path.join(BASE_DIR, 'templates')

TEMPLATE_DIRS = (
    TEMPLATE_PATH,
)

REGISTRATION_OPEN = True                    # If True, users can register
ACCOUNT_ACTIVATION_DAYS = 7                 # One-week activation window; you may, of course, use a different value.
REGISTRATION_AUTO_LOGIN = True              # If True, the user will be automatically logged in.
LOGIN_REDIRECT_URL = '/helikopter/'         # The page you want users to arrive at after they successful log in
LOGIN_URL = '/helikopter/accounts/login/'    # The page users are directed to if they are not logged in,

EMAIL_BACKEND = 'django.core.mail.backends.smtp.EmailBackend'

EMAIL_USE_TLS = True
EMAIL_HOST = 'smtp.gmail.com'
EMAIL_PORT = 587
EMAIL_HOST_USER = 'helikopterem.po.slasku'
EMAIL_HOST_PASSWORD = 'h3lik0pt3r3m'

REST_FRAMEWORK = {
    'DEFAULT_PERMISSION_CLASSES': ('rest_framework.permissions.IsAuthenticated',),
    'DEFAULT_AUTHENTICATION_CLASSES': (
        'rest_framework.authentication.BasicAuthentication',
        'rest_framework.authentication.SessionAuthentication',
    ),
    'DEFAULT_PAGINATION_CLASS': 'rest_framework.pagination.PageNumberPagination',
    'PAGE_SIZE': 25
}

CRISPY_TEMPLATE_PACK = 'bootstrap3'
