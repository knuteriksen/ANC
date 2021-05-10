'''
Remember to remove the sep= from the top of the csv file, manually
'''

import csv
import json
csvpaths=[r'log/tournament-20210507-163302-EnergySmall-B-domain.log.csv',
r'log/tournament-20210507-150731-NiceOrDie-A-domain.log.csv',
r'log/tournament-20210507-163450-FlightBooking-C-domain.log.csv',
r'log/tournament-20210507-163249-Phone-A-domain.log.csv' ]

jsonpaths = [r'results/energy_results.json',
r'results/niceordie_results.json',
r'results/flightbooking_results.json',
r'results/phone_results.json' ]

def make_json():
    for n, csvFilePath in enumerate(csvpaths):
        # create a dictionary
        data = {}
        names = {}

        # Open a csv reader called DictReader
        with open(csvFilePath, encoding='utf-8') as csvf:
            csvReader = csv.DictReader(csvf, delimiter=';')

            # Convert each row into a dictionary
            # and add it to data
            for i, rows in enumerate(csvReader):

                key = i
                data[key] = rows

                # Make agent names unique
                data[i]['Agent 1'] = data[i]['Agent 1'].split('@', 1)[0]
                data[i]['Agent 2'] = data[i]['Agent 2'].split('@', 1)[0]

                # Construct name dictionary and get components
                if data[i]['Agent 1'] not in names:
                    if len(data[i]['Agent 1']) > 100:
                        names[data[i]['Agent 1']] = "boa_"+str(i)

                        namestr = data[i]['Agent 1']
                        namestr = namestr.split('.')
                        bid = namestr[4].split('_',1)[0]
                        acc = namestr[8].split('-',1)[0]
                        om  = namestr[11].split('-',1)[0]
                        oms = namestr[14]

                        data[i].update({'bid':bid, 'acc':acc, 'om':om, 'oms':oms})

                    else:
                        names[data[i]['Agent 1']] = data[i]['Agent 1']
            # Change names
            for key in data:
                data[key]['Agent 1'] = names[data[key]['Agent 1']]
                data[key]['Agent 2'] = names[data[key]['Agent 2']]

        # Open a json writer, and use the json.dumps()
        # function to dump data
        with open(jsonpaths[n], 'w', encoding='utf-8') as jsonf:
            jsonf.write(json.dumps(data, indent=4))

# Driver Code

# Call the make_json function
make_json()
