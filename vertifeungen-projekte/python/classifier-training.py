import pandas
import numpy as np
import warnings
import matplotlib.pyplot as pyplot
from sklearn.preprocessing import StandardScaler, OneHotEncoder
from sklearn.model_selection import train_test_split
from sklearn import tree
from sklearn.neighbors import KNeighborsClassifier


# Configuration
warnings.filterwarnings('ignore')
pyplot.rcParams['figure.figsize'] = (10, 10)
pyplot.style.use('seaborn')

def get_prepared_dataset():
    # load dataset
    df_bank = pandas.read_csv('dataset/bank.csv')

    # drop duration column
    df_bank = df_bank.drop('duration', axis=1)

    # create a copy
    df_bank_ready = df_bank.copy()

    # scaling numberic data to avoid significant outlier presence
    scaler = StandardScaler()
    num_cols = ['age', 'balance', 'day', 'campaign', 'pdays', 'previous']
    df_bank_ready[num_cols] = scaler.fit_transform(df_bank[num_cols])

    # Encoding categorical data
    encoder = OneHotEncoder(sparse=False)
    cat_cols = ['job', 'marital', 'education', 'default', 'housing', 'loan', 'contact', 'month', 'poutcome']

    # Encode Categorical Data
    df_encoded = pandas.DataFrame(encoder.fit_transform(df_bank_ready[cat_cols]))
    df_encoded.columns = encoder.get_feature_names(cat_cols)

    # Replace Categotical Data with Encoded Data
    df_bank_ready = df_bank_ready.drop(cat_cols ,axis=1)
    df_bank_ready = pandas.concat([df_encoded, df_bank_ready], axis=1)

    # Encode target value
    df_bank_ready['deposit'] = df_bank_ready['deposit'].apply(lambda x: 1 if x == 'yes' else 0)

    return df_bank_ready

def build_decision_tree_classifier(x_train, y_train):
    decision_tree_model = tree.DecisionTreeClassifier(random_state=0)
    decision_tree_model.fit(x_train, y_train)
    return decision_tree_model

def build_kneighbors_classifier(x_train, y_train):
    kneighbors_model = KNeighborsClassifier()
    kneighbors_model.fit(x_train, y_train)
    return kneighbors_model

def evaluate_model(model, x_test, y_test):
    from sklearn import metrics

    # Predict Test Data 
    y_pred = model.predict(x_test)

    # Calculate accuracy, precision, recall, f1-score, and kappa score
    acc = metrics.accuracy_score(y_test, y_pred)
    prec = metrics.precision_score(y_test, y_pred)
    rec = metrics.recall_score(y_test, y_pred)
    f1 = metrics.f1_score(y_test, y_pred)
    kappa = metrics.cohen_kappa_score(y_test, y_pred)

    # Calculate area under curve (AUC)
    y_pred_proba = model.predict_proba(x_test)[::,1]
    fpr, tpr, _ = metrics.roc_curve(y_test, y_pred_proba)
    auc = metrics.roc_auc_score(y_test, y_pred_proba)

    # Display confussion matrix
    cm = metrics.confusion_matrix(y_test, y_pred)

    return {'acc': acc, 'prec': prec, 'rec': rec, 'f1': f1, 'kappa': kappa, 
            'fpr': fpr, 'tpr': tpr, 'auc': auc, 'cm': cm}

def print_evaluation(title, evaluation):
    print('=====================================================')
    print(title)
    print('=====================================================')
    print('Accuracy:', evaluation['acc'])
    print('Precision:', evaluation['prec'])
    print('Recall:', evaluation['rec'])
    print('F1 Score:', evaluation['f1'])
    print('Cohens Kappa Score:', evaluation['kappa'])
    print('Area Under Curve:', evaluation['auc'])
    print('Confusion Matrix:\n', evaluation['cm'])

if __name__ == '__main__':
    # prepare dataset
    bank_dataset = get_prepared_dataset()

    # Select Features
    feature = bank_dataset.drop('deposit', axis=1)

    # Select Target
    target = bank_dataset['deposit']

    # Split testing and training data
    x_train, x_test, y_train, y_test = train_test_split(feature , target, 
                                                    shuffle = True, 
                                                    test_size=0.2, 
                                                    random_state=1)

    # Train classifiers
    dt_classifier = build_decision_tree_classifier(x_train, y_train)
    kn_classifier = build_kneighbors_classifier(x_train, y_train)

    # Evaluate classifiers
    dtc_eval = evaluate_model(dt_classifier, x_test, y_test)
    knc_eval = evaluate_model(kn_classifier, x_test, y_test)

    # Display results
    print_evaluation("Decision tree classifier", dtc_eval)
    print_evaluation("K nearest neighbors classifier", knc_eval)