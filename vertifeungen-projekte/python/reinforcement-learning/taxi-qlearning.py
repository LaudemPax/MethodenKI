import gym
import numpy as np
import random

env = gym.make("Taxi-v3").env
q_table = np.zeros([env.observation_space.n,env.action_space.n])

# Q-Regel parameters
alpha = 0.1
gamma = 0.6
epsilon = 0.1

NUMBER_OF_EPISODES = 100000

print("Begin training")

for i in range(1, (NUMBER_OF_EPISODES + 1)):
    state = env.reset()

    epochs, penalties, reward = 0,0,0

    epoch_done = False

    while not epoch_done:
        if random.uniform(0, 1) < epsilon:
            action = env.action_space.sample() # Explore action space
        else:
            action = np.argmax(q_table[state]) # Exploit learned values

        next_state, reward, epoch_done, info = env.step(action) 

        old_value = q_table[state, action]
        next_max = np.max(q_table[next_state])
        
        new_value = (1 - alpha) * old_value + alpha * (reward + gamma * next_max)
        q_table[state, action] = new_value

        if reward == -10:
            penalties += 1

        state = next_state
        epochs += 1

    if i % 100 == 0:
        print(f"Episode: {i}")

print('Training done')

# TRAINING
# =================================================================
# TESTING

print('Running tests')
total_penalties = 0
total_epochs = 0
episodes = 100

for _ in range(episodes):
    state = env.reset()
    epochs, penalties, reward = 0, 0, 0
    
    done = False
    
    while not done:
        action = np.argmax(q_table[state])
        state, reward, done, info = env.step(action)

        if reward == -10:
            penalties += 1

        epochs += 1

        env.render()

    total_penalties += penalties
    total_epochs += epochs

print(f"Results after {episodes} episodes:")
print(f"Average timesteps per episode: {total_epochs / episodes}")
print(f"Average penalties per episode: {total_penalties / episodes}")