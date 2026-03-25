# 🚀 Hyper Universal Space Tactics

**Hyper Universal Space Tactics (HUST)** is a 2D space shooter game built with Java, focusing on **object-oriented programming (OOP)**, **clean architecture**, and the practical application of **design patterns** in a real-time game environment.

---

## 🎮 Gameplay

* Fast-paced arcade-style shooting mechanics
* Movement using **WASD / Arrow Keys**
* Primary fire + laser skill
* Boost / dash system with cooldown
* Multiple enemy types (basic, orbit, drone, boss)
* Time-based wave spawning system
* Projectile collision handling (player / enemy / projectile)
* Explosion and death visual effects
* Smooth rendering and responsive controls
* Dynamic combat experience


---

## 📸 Demo
![image](https://github.com/user-attachments/assets/6ec231f3-11e5-4b5d-aefd-dbdcbd1c16f7)



---


## 🧠 Technical Focus

This project is designed as a **learning-oriented game system**, emphasizing:

### Object-Oriented Programming (OOP)

* Encapsulation
* Inheritance
* Polymorphism
* Abstraction
* SOLID principles

### Design Patterns

* **Singleton** – `SoundManager`, `PoolManager`, `ImageLoader`, ...
* **Factory Method** – Projectile creation
* **State Pattern** – Game states (`Menu`, `Playing`, `GameOver`, ...)
* **Strategy Pattern** – Movement behaviors (`LinearMovement`, `MoveToTarget`, ...)
* **Object Pooling** – Efficient reuse of enemies, projectiles, effects

### Game Architecture

* Custom game loop implementation
* Separation of concerns (rendering, logic, input)
* Modular and scalable system design

---

## ⚙️ Technologies Used

* Java 21 (Core / Java2D)
* Maven
* OOP principles
* Custom rendering pipeline

---

## 📦 Prerequisites

* JDK 21+
* Windows / Linux / macOS with display support

---

## 📁 Project Structure

### Source Code

```
src/main/java/com/game/shooting2DGame
  core/        # game loop, engine
  state/       # menu / playing / pause / game over
  entity/      # player, enemy, wave manager
  projectiles/ # projectile types + manager
  movement/    # movement strategies
  weapon/      # weapon types
  render/      # renderers, camera, transition effects
  sound/       # audio management
  ui/          # HUD, button, slider, window/panel
  utils/       # vector, image loader, object pool
```

### Resources

```
src/main/resources
  aircraft/
  background/
  bullet/
  effect/
  font/
  hud/
  sound/
  ...
```

---

## ▶️ How to Run

### Windows

```bash
git clone https://github.com/quanghien1712/hyperspace-tactics-java.git
cd hyperspace-tactics-java
New-Item -ItemType Directory -Force target/manual-classes | Out-Null
$src = Get-ChildItem -Recurse src/main/java -Filter *.java | Select-Object -ExpandProperty FullName
javac -encoding UTF-8 -d target/manual-classes $src
java -cp "target/manual-classes;src/main/resources" com.game.shooting2DGame.Main
```

### MacOS / Linux
```
git clone https://github.com/quanghien1712/hyperspace-tactics-java.git
cd hyperspace-tactics-java
mkdir -p target/manual-classes
find src/main/java -name "*.java" > target/sources.txt
javac -encoding UTF-8 -d target/manual-classes @target/sources.txt
java -cp "target/manual-classes:src/main/resources" com.game.shooting2DGame.Main

```

### Or simply open the project in **VSCode / IntelliJ / Eclipse** and run the main class.

---

## 🎯 Learning Objectives

This project demonstrates:

* Applying OOP in a real-world system
* Designing a scalable Java application
* Implementing design patterns in game development
* Optimizing performance using object pooling and resource management

---

## 🚧 Future Improvements

* Advanced boss mechanics
* Skill / upgrade system
* Improved UI & animations
* Enhanced sound and visual polish
* Packaging to executable (.exe)

---

## 👤 Author

* **Quang Hien**
* Student at **Hanoi University of Science and Technology**

---

## ⭐ Acknowledgements

This project is built for learning and portfolio purposes, inspired by *Hyperspace Striker* on Steam.
