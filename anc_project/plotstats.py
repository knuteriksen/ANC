import json
import matplotlib.pyplot as plt

statspaths = ['stats/energy_stats.json',
'stats/niceordie_stats.json',
'stats/flightbooking_stats.json',
'stats/phone_stats.json'
]


stats = {}
data = {}

for path in statspaths:
	# Opening JSON file
	with open(path) as json_file:
		stats = json.load(json_file)

	for key in stats:
		if key not in data:
			data[key] = {'avgUtil': stats[key]['avgUtil'],'avgDtp':stats[key]['avgDtp'], 'avgDtn':stats[key]['avgDtn']}
		else:
			data[key]['avgUtil'] = (float(data[key]['avgUtil']) + float(stats[key]['avgUtil']))/2.0
			data[key]['avgDtp'] = (float(data[key]['avgDtp']) + float(stats[key]['avgDtp']))/2.0
			data[key]['avgDtn'] = (float(data[key]['avgDtn']) + float(stats[key]['avgDtn']))/2.0


fig, ax = plt.subplots(1)
for key in data:
    ax.bar(key, data[key]['avgUtil'])
plt.show()
