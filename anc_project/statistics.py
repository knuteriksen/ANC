import json
from scipy import stats
from scikit_posthocs import posthoc_dunn

all_utils_path = 'stats/all_utils'

data = {}
populations = []

with open(all_utils_path) as json_file:
    data = json.load(json_file)

for key in data:
    if 'boa' in key:
        populations.append([float(i) for i in data[key]])

print(stats.kruskal(*populations))

print(posthoc_dunn(populations, p_adjust="bonferroni"))
