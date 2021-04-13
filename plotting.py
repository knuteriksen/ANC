import matplotlib.pyplot as plt
from matplotlib.colors import ListedColormap
import numpy as np

k_values=[0.85, 0.98, 0.88, 0.00, 0.97, 0.00, 0.88, 0.82]
o_values=[0.57, 0.49, 0.71, 0.00, 0.47, 0.00, 0.68, 0.74]
colours =ListedColormap(['tab:blue', 'tab:orange', 'tab:green', 'tab:red', 'tab:purple', 'tab:brown', 'tab:pink', 'tab:cyan'])
c_vals  =[0,1,2,3,4,5,6,7]
labels  =['Boulware', 'Conceder', 'FullAgent', 'Hardliner', 'T-Conceder', 'Gangster', 'NiceT4T', 'Agent33']

plt.rcParams["figure.figsize"] = (8,8)
scatter = plt.scatter(k_values, o_values, c=c_vals, cmap=colours, marker='x')
plt.legend(handles=scatter.legend_elements()[0], labels=labels, bbox_to_anchor=(0.5, 1.00), loc='upper center', ncol=4)
plt.xlim(0.7, 1)
plt.ylim(0.4, 1)
plt.grid(linewidth=0.25)
plt.title("Knut as Party 1")
plt.ylabel("Opponent Utility")
plt.xlabel("Knut Utility")
plt.show()



k_values=[0.88, 1.00, 0.98, 0.00, 1.00, 0.86, 0.84, 0.00]
o_values=[0.79, 0.63, 0.72, 0.00, 0.69, 0.85, 0.66, 0.00]
colours =ListedColormap(['tab:blue', 'tab:orange', 'tab:green', 'tab:red', 'tab:purple', 'tab:brown', 'tab:pink', 'tab:cyan'])
c_vals  =[0,1,2,3,4,5,6,7]
labels  =['Boulware', 'Conceder', 'FullAgent', 'Hardliner', 'T-Conceder', 'Gangster', 'NiceT4T', 'Agent33']

plt.rcParams["figure.figsize"] = (8,8)
scatter = plt.scatter(k_values, o_values, c=c_vals, cmap=colours, marker='x')
plt.legend(handles=scatter.legend_elements()[0], labels=labels, bbox_to_anchor=(0.5, 1.00), loc='upper center', ncol=4)
plt.xlim(0.7, 1)
plt.ylim(0.4, 1)
plt.grid(linewidth=0.25)
plt.title("Knut as Party 2")
plt.ylabel("Opponent Utility")
plt.xlabel("Knut Utility")
plt.show()


y = [0.12, 0.00, 0.02, 1.04, 0.03, 1.04, 0.00, 0.06]
labels  =['Boulware', 'Conceder', 'FullAgent', 'Hardliner', 'T-Conceder', 'Gangster', 'NiceT4T', 'Agent33']
plt.rcParams["figure.figsize"] = (8,4)
plt.bar(labels,y,color='tab:blue')
plt.title("Distance to Pareto - Knut as Party 1")
plt.ylim(0, 0.3)
plt.show()


y = [0.37, 0.47, 0.23, 1.22, 0.49, 1.22, 0.26, 0.19]
labels  =['Boulware', 'Conceder', 'FullAgent', 'Hardliner', 'T-Conceder', 'Gangster', 'NiceT4T', 'Agent33']
plt.rcParams["figure.figsize"] = (8,4)
plt.bar(labels,y,color='tab:blue')
plt.title("Distance to Nash - Knut as Party 1")
plt.ylim(0, 0.6)
plt.show()

y = [0.0238, 0.0000, 0.0000, 1.0360, 0.0000, 0.0000, 0.1400, 1.0360]
labels  =['Boulware', 'Conceder', 'FullAgent', 'Hardliner', 'T-Conceder', 'Gangster', 'NiceT4T', 'Agent33']
plt.rcParams["figure.figsize"] = (8,4)
plt.bar(labels,y,color='tab:blue')
plt.title("Distance to Pareto - Knut as Party 2")
plt.ylim(0, 0.3)
plt.show()

y = [0.0483, 0.1454, 0.0792, 1.2159, 0.1454, 0.0960, 0.1531, 1.2159]
labels  =['Boulware', 'Conceder', 'FullAgent', 'Hardliner', 'T-Conceder', 'Gangster', 'NiceT4T', 'Agent33']
plt.rcParams["figure.figsize"] = (8,4)
plt.bar(labels,y,color='tab:blue')
plt.title("Distance to Nash - Knut as Party 2")
plt.ylim(0, 0.3)
plt.show()
