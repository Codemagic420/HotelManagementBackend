# Security Guidelines

## 🔐 Environment Variables & Secrets

### Critical: Never commit `.env` files

The `.env` file contains sensitive credentials and **MUST NOT** be committed to git:
- `.env` is in `.gitignore`
- `.env.local` is in `.gitignore`
- Only `.env.example` is committed (with placeholder values)

### Setup for Development

**IMPORTANT:** The `.env` file is NOT in the repository (it's in `.gitignore`). You must create it locally.

1. **Copy the example file:**
   ```bash
   cp .env.example .env
   ```

2. **For development, the default passwords in `.env` work as-is:**
   - `admin123`, `staff123`, `cleaner123` (Spring Boot users)
   - `apppassword123` (Database user)
   - `secure_mongo_password_dev` (MongoDB user)
   - These match the hardcoded defaults in SQL scripts
   
3. **For production, CHANGE ALL passwords in `.env`:**
   ```bash
   # Generate strong passwords (32+ characters)
   openssl rand -base64 32
   ```
   
   Then update `.env`:
   ```env
   MYSQL_ROOT_PASSWORD=<strong-password-1>
   MYSQL_PASSWORD=<strong-password-2>
   MYSQL_ADMIN_PASSWORD=<strong-password-3>
   MYSQL_STAFF_PASSWORD=<strong-password-4>
   CLEANER_PASSWORD=<strong-password-5>
   MONGO_ROOT_PASSWORD=<strong-password-6>
   NEO4J_PASSWORD=<strong-password-7>
   ```

4. **Environment variable behavior:**
   - If `.env` is missing, docker-compose uses **hardcoded defaults** (development-only)
   - If `.env` exists, docker-compose uses those values
   - DataInitializer REQUIRES all user passwords to be set (will fail clearly if missing)

### Production Deployment

**For cloud/production:**
1. Do NOT use `.env` files
2. Set environment variables in your deployment platform (Docker Swarm secrets, Kubernetes secrets, AWS Secrets Manager, etc.)
3. Rotate passwords regularly
4. Use strong password generation:
   ```bash
   # Generate secure 32-character password
   openssl rand -base64 32
   ```

## 🛡️ Authentication Security

### User Accounts

Three role-based user accounts are auto-created on first startup:

| Username | Password Env Var | Role | Access |
|----------|-----------------|------|--------|
| `admin` | `MYSQL_ADMIN_PASSWORD` | ADMIN | All endpoints |
| `staff` | `MYSQL_STAFF_PASSWORD` | STAFF | Guest-facing operations |
| `cleaner1-20` | `CLEANER_PASSWORD` | CLEANER | Cleaning operations |

### Login Flow

1. POST `/api/auth/login` with credentials
   ```json
   {
     "username": "admin",
     "password": "your_admin_password"
   }
   ```

2. Receive JWT token:
   ```json
   {
     "token": "eyJhbGc...",
     "type": "Bearer",
     "expiresIn": 86400
   }
   ```

3. Use token in Authorization header:
   ```
   Authorization: Bearer eyJhbGc...
   ```

### JWT Configuration

- **Expiration:** 24 hours (86400 seconds)
- **Stored in:** SecurityContext (stateless)
- **No refresh tokens:** Login again when expired

## 🚫 What You Should NOT Do

❌ **DO NOT:**
- Hardcode passwords in code
- Commit `.env` files with real credentials
- Use weak passwords (< 12 characters)
- Share credentials in chat/email
- Use the same password for multiple systems
- Enable anonymous access to protected endpoints

✅ **DO:**
- Use environment variables
- Generate strong passwords (32+ characters)
- Rotate credentials regularly
- Use `.env.example` for documentation
- Implement rate limiting on login endpoints
- Log authentication attempts
- Use HTTPS in production

## 🔍 Security Checklist

- [ ] `.env` is in `.gitignore`
- [ ] No passwords hardcoded in source files
- [ ] `MYSQL_ADMIN_PASSWORD` is set (not using default)
- [ ] `MYSQL_STAFF_PASSWORD` is set (not using default)
- [ ] `CLEANER_PASSWORD` is set (not using default)
- [ ] All database passwords are strong (12+ characters, mixed case)
- [ ] DataInitializer fails loudly if env vars are missing
- [ ] No `CHANGE_ME_*` values in actual `.env` file
- [ ] Git history is clean (no credentials pushed)

## 📋 Verify Security Before Pushing

```bash
# Check that .env is ignored (should not appear)
git status | grep ".env"

# Check for hardcoded passwords in code (only dev defaults allowed)
grep -r "password123\|admin123\|staff123\|cleaner123" src/main/java/

# Check git history for secrets
git log -p | grep -i "mysql_password\|mongo_root\|neo4j_password" | head -5

# Verify .env.example has no real credentials
grep "MYSQL_PASSWORD=" .env.example  # Should show example/placeholder only
```

### Pre-Push Checklist for Public Branch

Before pushing to a public repository:

```bash
# 1. Verify .env is in .gitignore
grep "^\.env" .gitignore

# 2. Verify .env is not in git history
git log --all --full-history --format="%H %s" -- .env

# 3. Verify no actual credentials in git
git grep -i "apppassword123\|admin123" HEAD -- src/  # Only in DataInitializer comments is OK

# 4. Verify .env.example exists with placeholder values
ls -la .env.example && grep "PRODUCTION:" .env.example
```

**If credentials were committed to git history**, they are compromised. Follow remediation steps below.

## 🚨 If You Accidentally Commit Credentials

1. **Immediately rotate all passwords** in production
2. Remove from git history:
   ```bash
   git filter-branch --tree-filter 'rm -f .env' HEAD
   git push origin --force-with-lease
   ```
3. Notify your team
4. Review who has access to the repository

## 📞 Questions?

If you see a blank/missing `.env` file:
1. Check `.env.example` exists
2. Copy it: `cp .env.example .env`
3. Edit all `CHANGE_ME_*` values with real passwords
4. Never commit `.env`
