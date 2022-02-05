# SUSTech CS102A Project: Othello

<!-- Score: 100/100 -->

### Basic Requirements

| Requirements                           | Implemented? |
| :------------------------------------- | :- |
| **Task 1: Initialize Game**            | ✅ |
| initialize a new chess game            | ✅ |
| display the status of the game         | ✅ |
| restart a game                         | ✅ |
| **Task 2: Load and Save a Game**       | ✅ |
| load an existing game                  | ✅<sup>[1]</sup> |
| save the current game                  | ✅<sup>[1]</sup> |
| error check                            | ✅<sup>[2]</sup> |
| **Task 3: Play the game**              | ✅ |
| detect the winning status              | ✅ |
| allow disks to be flipped              | ✅ |
| cheat mode                             | ✅ |
| **Task 4: Graphical User Interface**   | ✅ |
| graphical user interface               | ✅<sup>[3]</sup> |

<sub><sup>[1] Save to a binary file rather than a text file</sup></sub>  
<sub><sup>[2] Error is checked but only printed to console</sup></sub>  
<sub><sup>[3] Using [Compose Multiplatform](https://www.jetbrains.com/zh-cn/lp/compose-mpp/) with Kotlin</sup></sub>

### Bonus
1. **AI**
   Implement using minimax algorithm with alpha-beta cut off. And implement different difficulty levels with evaluators of different strategy.

2. **Online mode**
   - Support multi-player mode (using TCP connection).
   - Undo and restart are supported in online mode (need opponent's approval).
   - Auto discover server in the LAN, so no need to input IP and port manually.
   - The connection is encrypted.
   - Room password is supported (and MITM attack can be prevented with password).

3. **Undo operation**, also support infinity undo operations

4. **Show possible moves**

5. **Pack the game as an executable** (thanks to the framework, no additional work is required)

6. **Replay**, You can replay a game from a saved game
