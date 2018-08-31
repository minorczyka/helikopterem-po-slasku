from django import template
from helikopter.models import Person

register = template.Library()


@register.inclusion_tag('helikopter/summary.html')
def get_summary_list(user):
    result = user.person.statsSummary()
    summary = []
    for it in result:
        person = Person.objects.filter(id=it['id']).first()
        summary.append({'person': person, 'points': it['points']})
    return {'summary': summary, 'user': user}
