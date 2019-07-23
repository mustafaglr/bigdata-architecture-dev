import numpy as np
import pandas as pd
from scipy import stats
from sklearn.model_selection import train_test_split
from pymongo import MongoClient

RANDOM_SEED = 13

TIME_STEP = 170

SEGMENT_TIME_SIZE = 20
0
data = {}
# Get data from database and delete unused parts
client = MongoClient("localhost", 27017)
db = client.get_database("people")
persondetails = db.get_collection("persondetails")

data = pd.DataFrame(list(persondetails.find({}, {"id" : 0})))
del data['_id']
del data['timestamp']
del data['user']

# DATA PREPROCESSING
data_convoluted = []
labels = []

# Slide a "SEGMENT_TIME_SIZE" wide window with a step size of "TIME_STEP"
for i in range(0, len(data) - SEGMENT_TIME_SIZE, TIME_STEP) :
    x = data['x-acceleration'].values[i : i + SEGMENT_TIME_SIZE]
    y = data['y-accel'].values[i : i + SEGMENT_TIME_SIZE]
    z = data['z-accel'].values[i : i + SEGMENT_TIME_SIZE]
    data_convoluted.append([x, y, z])

    # Label for a data window is the label that appears most commonly
    label = stats.mode(data['activity'][i : i + SEGMENT_TIME_SIZE])[0][0]
    labels.append(label)

# Convert to numpy
data_convoluted = np.asarray(data_convoluted, dtype=np.float32).transpose(0, 2, 1)

# One-hot encoding
labels = np.asarray(pd.get_dummies(labels), dtype=np.float32)
print("Convoluted data shape: ", data_convoluted.shape)
print("Labels shape:", labels.shape)

# SPLIT INTO TRAINING AND TEST SETS
X_train, X_test, y_train, y_test = train_test_split(data_convoluted, labels, test_size=0.2, random_state=RANDOM_SEED)
print("X train size: ", len(X_train))
print("X test size: ", len(X_test))
print("y train size: ", len(y_train))
print("y test size: ", len(y_test))

nsamples, nx, ny = X_test.shape
x_test = X_test.reshape((nsamples,nx*ny))

nsamples, nx, ny = X_train.shape
x_trainnig = X_train.reshape((nsamples,nx*ny))

from sklearn.neighbors import KNeighborsClassifier
neigh = KNeighborsClassifier(n_neighbors=1)
neigh.fit(x_trainnig, y_train)
knn=neigh.score(x_test,y_test)
