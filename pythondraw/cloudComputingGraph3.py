import matplotlib.pyplot as plt
import sys
import numpy as np
from mpl_toolkits.mplot3d import Axes3D
from matplotlib import cm
from matplotlib.ticker import LinearLocator, FormatStrFormatter

# Range for RDD is 1 - 5 (0.2 - 1.0)
# Collect for each size, for each algorithm the result
f = open('WorkLoad-2.csv', 'r')

# Read all lines, saving in an array
data = f.readlines()

# Initialize the result array, and a separate array to count the amount of instances.


arr = []
column = 5
for j in range(1, column):
    arr.append([])

i = 0
# Loop through all the data

for entry in data:
    # print("i= ", i)
    if (len(entry) > 1):
        entry_data = entry[1:len(entry) - 1].split(",")
        print(entry_data)

        # for j in range(0,len(entry_data)) :
        #     result[i, j] = int(entry_data[j])
        #     print(result[i, j])

        count = 0
        flag = sys.maxsize


        for ele in entry_data:
            #
            # print("ele= ", ele)
            if(ele!=""):
                arr[count].append(int(ele))
                count += 1
            # print(arr[count][i])


        while count < 2:
            arr[count].append(0)
            count += 1

        i += 1

print(arr[0])
print(arr[1])
print(len(arr[1])==len(arr[0]))

x = list(range(0, len(arr[0])))

# print(x)

# flag=0
# for ele in arr[2]:
#     if ele != 0:
#         break
#     flag+=1
#
# flag2=2
# for ele in arr[3]:
#     if ele != 0:
#         break
#     flag2+=1


# plt.plot((flag-1, flag-1), (0, 180), '--')
ax = plt.gca()
ax.tick_params(axis='x', which='major', labelsize=35)
ax.tick_params(axis='y', which='major', labelsize=35)

plt.plot((18, 18), (0, 20), '--')

plt.plot(x, arr[0],label='Node 1')
plt.plot(x, arr[1],label='Node 2')


plt.title('Auto Scaling Down',size=35)
plt.xlabel('Time',size=35)
plt.ylabel('Number of Tasks on Each Node',size=35)
plt.legend(bbox_to_anchor=(1.05, 1), loc='upper right', borderaxespad=0.,prop={'size':25})
plt.show()
