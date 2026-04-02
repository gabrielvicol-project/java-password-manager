# 🔐 Java Password Manager

![Status](https://img.shields.io/badge/status-v0.6--beta-orange)
![Java](https://img.shields.io/badge/Java-17%2B-blue?logo=openjdk)
![License](https://img.shields.io/badge/license-MIT-green)
![Platform](https://img.shields.io/badge/platform-CLI-lightgrey)
![Build](https://img.shields.io/badge/build-manual%20javac-yellow)

> A minimalist, privacy-first password manager that runs entirely in your terminal — no cloud, no telemetry, no dependencies you didn't ask for.

---

## 📋 Description

**Java Password Manager** is a command-line tool for storing and managing credentials locally on your machine. The philosophy is simple: your passwords stay on your disk, under your control, encrypted at rest.

Designed for developers and power users who are comfortable in a terminal environment and prefer a lightweight alternative to GUI-based or cloud-synced password managers. No account registration, no internet connection required.

> ⚠️ **Current State:** This is a **v0.6 Beta**. Core CRUD functionality and basic authentication are in place, but encryption at rest is not yet implemented. See the [Security Disclaimer](#-security-disclaimer) before use.

---

## ✅ Core Features (v0.6)

| Feature | Status | Notes |
|---|---|---|
| Master Password | ✅ Working | Plain-text storage — hashing planned for v0.7 |
| AES Encryption | 🟡 Planned | Depends on master password (v0.8) |
| Add Credential | ✅ Working | Persisted to `passwords.txt` |
| Remove Credential | ✅ Working | Temp-file swap strategy |
| Search by Service | ✅ Working | Case-insensitive match |
| Show All Credentials | ✅ Working | Plain-text read from file |
| CLI Menu Interface | ✅ Working | Interactive loop via `Scanner` |

---

## 🚀 Quick Start

### Prerequisites

- **Java 17+** — verify with `java -version`
- No external dependencies required for v0.6

### Installation

```bash
# 1. Clone the repository
git clone https://github.com/your-username/java-password-manager.git
cd java-password-manager

# 2. Compile the source files
javac -d out src/password_manager/*.java

# 3. Run the application
java -cp out password_manager.Main
```

> If your project uses Maven, replace steps 2–3 with:
> ```bash
> mvn compile exec:java -Dexec.mainClass="password_manager.Main"
> ```

### First Run

On the very first launch, the vault has no master password yet. You will be prompted to create one:

```
[INFO] New vault detected. Set your master password: ********
[SUCCESS] Master password saved.
```

On every subsequent launch, the application authenticates before showing the menu:

```
[MASTER] Enter master password: ********

===============================
      PASSWORD MANAGER CLI
===============================
1. Add New Account
2. Remove Account
3. Search Account
4. Show All Accounts
5. Exit
Your choice:
```

If the wrong password is entered, the application exits immediately without granting access.

---

## 🖥️ CLI Command Reference

| Menu Option | Action | Input Required |
|---|---|---|
| `1` — Add Account | Saves a new credential entry | Service name, Username, Password |
| `2` — Remove Account | Deletes a credential by service name | Service name + confirmation (`y`/`n`) |
| `3` — Search Service | Looks up credentials by service name | Service name |
| `4` — Show All | Lists all stored credentials | None |
| `5` — Exit | Terminates the application | None |

**Example session:**

```
Inserisci master password: ********

===============================
      PASSWORD MANAGER CLI
===============================
Your choice: 1

--- ADDING NEW CREDENTIALS ---
➤ Service Name: GitHub
➤ Username: mario.rossi
➤ Password: s3cr3t!
[SUCCESS] Account for GitHub saved correctly.
```

---

## 🏗️ Technical Architecture

### Data Model

Each credential is represented by the `Credential` class with three fields:

```
Service | Username | Password
```

### Authentication Layer (v0.6)

On startup, `Main.java` calls `pm.authenticate()` before entering the menu loop. The method handles two cases:

- **First run** — `master.txt` does not exist or is empty. The user is prompted to set a master password, which is written to `master.txt` and the vault opens.
- **Subsequent runs** — the stored password is read from `master.txt` and compared against the user's input. On mismatch, the method returns `false` and the application exits.

```
master.txt   ← stores master password (plain text — hashing planned for v0.7)
passwords.txt ← stores credentials (plain text — AES planned for v0.8)
```

> ⚠️ **Known limitation:** both files are currently plain text. See [Security Disclaimer](#-security-disclaimer).

### Persistence Layer

Credentials are serialized as semicolon-delimited plain text and appended to `passwords.txt` in the working directory:

```
GitHub;mario.rossi;s3cr3t!
Netflix;mario.rossi;p4ssw0rd
```

**Read operations** use a `BufferedReader` with line-by-line parsing and a `String.split(";")` strategy.

**Delete operations** use a temp-file swap: the application rewrites all non-matching lines to `temp.txt`, then replaces the original file — a standard safe-delete pattern for flat-file storage.

### Project Structure

```
java-password-manager/
├── src/
│   └── password_manager/
│       ├── Main.java             # Entry point, auth check, CLI loop
│       ├── PasswordManager.java  # CRUD operations, file I/O, authenticate()
│       └── Credential.java       # Data model (POJO)
├── passwords.txt                 # Generated at runtime (gitignored)
├── master.txt                    # Generated at first run (gitignored)
└── README.md
```

---

## 🗺️ Roadmap to v1.0

| Version | Feature | Priority | Status |
|---|---|---|---|
| **v0.6** | Master Password authentication | 🔴 High | ✅ Done |
| **v0.7** | Master password hashing (SHA-256 / BCrypt) | 🔴 High | 🟡 Next |
| **v0.7** | Input validation & robust exception handling | 🔴 High | 🟡 Next |
| **v0.8** | AES-256 encryption for stored credentials | 🔴 High | ⬜ Planned |
| **v0.8** | Migrate from flat-file to SQLite | 🟡 Medium | ⬜ Planned |
| **v0.9** | JUnit 5 unit test suite | 🟡 Medium | ⬜ Planned |
| **v0.9** | Advanced password generator (length, charset, entropy) | 🟡 Medium | ⬜ Planned |
| **v1.0** | Maven/Gradle proper build system | 🟢 Low | ⬜ Planned |
| **v1.0** | Clipboard copy (no terminal echo of passwords) | 🟢 Low | ⬜ Planned |
| **v1.0** | Export / import encrypted backup | 🟢 Low | ⬜ Planned |

Contributions and issue reports are welcome — see [CONTRIBUTING.md](CONTRIBUTING.md) if present.

---

## 🔒 Security Disclaimer

> **⚠️ Please read before use.**

This project is currently in **v0.6 Beta**. The following security properties do **not yet hold**:

- The master password is stored in `master.txt` as **plain text** — hashing (SHA-256/BCrypt) is planned for v0.7
- Credentials in `passwords.txt` are stored as **plain text** — AES-256 encryption is planned for v0.8
- No protection against concurrent file access or corruption
- Exception handling is minimal and may leak partial state

**Recommendations:**

- Do **not** use this tool to store credentials for critical accounts (banking, email, work systems)
- Keep `passwords.txt` and `master.txt` out of any synced or shared folder (Dropbox, OneDrive, Git)
- Add both files to your `.gitignore` immediately:
  ```
  # .gitignore
  passwords.txt
  master.txt
  temp.txt
  ```
- Use this software **at your own risk** — no warranty is provided

This disclaimer will be updated as security features land in subsequent releases.

---

## 📄 License

This project is released under the [MIT License](LICENSE).

---

*Built with ☕ Java — keeping it local, keeping it simple.*
