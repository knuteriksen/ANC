# importing the module
import json

resultspath = ['results/energy_results.json',
'results/niceordie_results.json',
'results/flightbooking_results.json',
'results/phone_results.json' ]

statspaths = ['stats/energy_stats.json',
'stats/niceordie_stats.json',
'stats/flightbooking_stats.json',
'stats/phone_stats.json'
]

all_utils_path = 'stats/all_utils.json'

utilspaths = ['stats/energy_u.json',
'stats/niceordie_u.json',
'stats/flightbooking_u.json',
'stats/phone_u.json']



all_utils = {}

for i, jsonRFilePath in enumerate(resultspath):
	data = {}
	stats = {}
	utils = {}

	# Opening JSON file
	with open(jsonRFilePath) as json_file:
		data = json.load(json_file)

	for key in data:
		if (data[key]['Exception'] != ""):
			continue
		dtp = float(data[key]['Dist. to Pareto'])
		dtn = float(data[key]['Dist. to Nash'])
		swf = float(data[key]['Social Welfare'])
		agent1 = data[key]['Agent 1']
		agent1_util = float(data[key]['Utility 1'])

		if (data[key]['Agreement'] == 'Yes'):
			agreement = 1
		else:
			agreement = 0

		if agent1 not in stats:
			stats[agent1] = {
			'avgUtil': agent1_util,
			'utils': [agent1_util],
			'avgDtp': dtp,
			'dtps': [dtp],
			'avgDtn': dtn,
			'dtns': [dtn],
			'agreements': agreement,
			'avgSwf': swf,
			'swfs': [swf]
			}

			try:
				stats[agent1].update({'bid':data[key]['bid'],
				'acc':data[key]['acc'],
				'om':data[key]['om'],
				'oms':data[key]['oms']}
				)
			except Exception as e:
				pass

		else:
			stats[agent1]['avgUtil'] = float(stats[agent1]['avgUtil']) + agent1_util
			stats[agent1]['utils'].append(agent1_util)
			stats[agent1]['avgDtp'] = float(stats[agent1]['avgDtp']) + dtp
			stats[agent1]['dtps'].append(dtp)
			stats[agent1]['avgDtn'] = stats[agent1]['avgDtn'] + dtn
			stats[agent1]['dtns'].append(dtn)
			stats[agent1]['avgSwf'] = stats[agent1]['avgSwf'] + swf
			stats[agent1]['swfs'].append(swf)
			stats[agent1]['agreements'] = stats[agent1]['agreements'] + agreement

		agent2 = data[key]['Agent 2']
		agent2_util = float(data[key]['Utility 1'])

		if agent2 not in stats:
			stats[agent2] = {
			'avgUtil': agent2_util,
			'utils': [agent2_util],
			'avgDtp': dtp,
			'dtps': [dtp],
			'avgDtn': dtn,
			'dtns': [dtn],
			'agreements': agreement,
			'avgSwf': swf,
			'swfs': [swf]
			}

		else:
			stats[agent2]['avgUtil'] = stats[agent2]['avgUtil'] + agent2_util
			stats[agent2]['utils'].append(agent2_util)
			stats[agent2]['avgDtp'] = stats[agent2]['avgDtp'] + dtp
			stats[agent2]['dtps'].append(dtp)
			stats[agent2]['avgDtn'] = stats[agent2]['avgDtn'] + dtn
			stats[agent2]['dtns'].append(dtn)
			stats[agent2]['avgSwf'] = stats[agent2]['avgSwf'] + swf
			stats[agent2]['swfs'].append(swf)
			stats[agent2]['agreements'] = stats[agent2]['agreements'] + agreement

		if agent1 not in all_utils:
			all_utils[agent1] = [agent1_util]
		else:
			all_utils[agent1].append(agent1_util)

		if agent2 not in all_utils:
			all_utils[agent2] = [agent2_util]
		else:
			all_utils[agent2].append(agent2_util)


	# Calculate average
	for key in stats:
		stats[key]['avgUtil'] = stats[key]['avgUtil']/len(stats[key]['utils'])
		stats[key]['avgDtp'] = stats[key]['avgDtp']/len(stats[key]['dtps'])
		stats[key]['avgDtn'] = stats[key]['avgDtn']/len(stats[key]['dtns'])
		stats[key]['avgSwf'] = stats[key]['avgSwf']/len(stats[key]['swfs'])

		utils[key] = stats[key]['utils']

	# Store as json
	with open(statspaths[i], 'w', encoding='utf-8') as jsonf:
		jsonf.write(json.dumps(stats, indent=4))

	with open(utilspaths[i], 'w', encoding='utf-8') as jsonf:
		jsonf.write(json.dumps(utils, indent=4))


with open(all_utils_path, 'w', encoding='utf-8') as jsonf:
	jsonf.write(json.dumps(all_utils, indent=2))
