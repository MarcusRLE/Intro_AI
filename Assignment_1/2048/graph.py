import matplotlib.pyplot as plt
import numpy as np

x_values = np.array(range(1, 11))
y_values = np.array(range(1, 11))

X, Y = np.meshgrid(x_values, y_values)
Z = np.array([
    [2096.8, 2048.8, 1563.2, 2130.4, 2284, 2695.2, 2777.6, 1570.4, 1379.2, 2221.6],
    [33182.4, 25039.2, 26476.8, 16618.4, 36318.4, 22936.8, 33852.8, 47521.6, 26018.4, 36501.6],
    [45212.8, 44274.4, 50996, 24717.6, 22811.2, 21158.4, 31979.2, 24909.6, 26875.2, 30113.6],
    [21525.6, 36455.2, 24289.6, 34461.6, 33733.6, 31536, 33411.2, 21969.6, 45096, 33575.2],
    [21032, 34313.6, 38190.4, 26596, 22904.8, 29428.8, 36644.8, 46375.2, 17618.4, 23552.8],
    [22308.8, 28807.2, 39984, 39655.2, 38851.2, 33973.6, 45858.4, 38988.8, 30959.2, 31776],
    [22557.6, 34501.6, 34609.6, 40545.6, 24404.8, 29256, 44876.8, 29484, 28802.4, 43728.8],
    [26950.4, 22888, 26279.2, 26715.2, 33765.6, 44428, 36025.6, 30588, 36344.8, 24430.4],
    [27051.2, 35541.6, 38971.2, 31424, 22442.4, 42176, 28362.4, 35035.2, 25630.4, 22428.8],
    [37009.6, 31806.4, 30338.4, 34038.4, 26752.8, 32204.8, 34406.4, 51092, 30500, 46732.8],
])

fig = plt.figure(figsize=(10, 7))
ax = fig.add_subplot(111, projection='3d')

ax.plot_surface(X, Y, Z, cmap='viridis', edgecolor='k')

ax.set_xlabel("b value")
ax.set_ylabel("a value")
ax.set_zlabel("Highscore")
ax.set_title("3D Plot of Snake and Empty cell Heuristic")

plt.show()

