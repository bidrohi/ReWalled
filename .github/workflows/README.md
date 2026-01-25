# ReWalled GitHub Actions Workflows

This directory contains the complete CI/CD pipeline for the ReWalled Android application.

## 📋 Workflow Overview

### 1. CI Pipeline (`ci.yml`)
**Triggers:** Push to `main` branch, Pull Requests to `main`

**Jobs:**
- **Test:** Runs `./gradlew allTests` with artifact upload
- **Build:** Builds debug APK and release bundle
- **Code Quality:** Runs `./gradlew check` with detekt reports
- **Security Scan:** Runs Trivy vulnerability scanner

**Outputs:**
- Test results and reports
- Debug APK (30-day retention)
- Release bundle (90-day retention)
- Security scan results

### 2. Internal Deployment (`deploy-internal.yml`)
**Trigger:** Manual workflow dispatch

**Features:**
- Configurable version name and release notes
- Optional test skipping for emergency releases
- Automatic keystore and service account handling
- Deployment status tracking
- Artifact upload for backup

**Requirements:**
- All required secrets configured
- Successful CI checks (recommended)

### 3. Beta Promotion (`promote-beta.yml`)
**Trigger:** Manual workflow dispatch

**Options:**
- Custom release notes
- Promotion status: completed/halted/draft

**Features:**
- Validates Internal deployment exists
- Creates GitHub release for Beta
- Deployment status tracking
- Branch protection integration

### 4. Production Promotion (`promote-production.yml`)
**Trigger:** Manual workflow dispatch

**Options:**
- Custom release notes
- GitHub release creation
- Slack notifications
- Rollback capability

**Features:**
- Validates Beta deployment exists
- Rollback to previous version
- Multi-channel notifications (Slack, Email, GitHub)
- Production deployment tracking

### 5. Dependencies Check (`dependencies-check.yml`)
**Triggers:** Daily schedule, manual dispatch

**Features:**
- Dependency review for PRs
- OWASP vulnerability scanning
- Automated update notifications
- Security issue creation

## 🔧 Setup Instructions

### Required GitHub Secrets

Add these secrets to your GitHub repository settings:

#### Essential Secrets
```bash
SIGNING_KEYSTORE_BASE64    # Base64 encoded release keystore
SIGNING_ALIAS             # Keystore alias
SIGNING_PASSWORD          # Keystore password
PLAY_SERVICE_ACCOUNT_JSON  # Google Play service account JSON
GOOGLE_SERVICES_JSON      # Firebase configuration for Android
```

#### Optional Secrets
```bash
ANDROID_PACKAGE_NAME      # Full package name (defaults to com.bidyut.tech.rewalled)
SLACK_WEBHOOK_URL        # Slack notifications for production releases
SMTP_SERVER             # Email notifications server
SMTP_PORT               # Email notifications port
SMTP_USERNAME           # Email notifications username
SMTP_PASSWORD           # Email notifications password
NOTIFICATION_EMAIL      # Email address for notifications
```

### Keystore Setup

1. **Create or obtain your release keystore:**
   ```bash
   keytool -genkey -v -keystore rewalled-release.jks -keyalg RSA -keysize 2048 -validity 10000 -alias rewalled
   ```

2. **Encode keystore for GitHub:**
   ```bash
   base64 -i rewalled-release.jks | pbcopy  # macOS
   base64 -w 0 rewalled-release.jks         # Linux
   ```

3. **Add to GitHub Secrets:**
   - Copy the base64 output
   - Add as `SIGNING_KEYSTORE_BASE64` secret
   - Add alias as `SIGNING_ALIAS`
   - Add password as `SIGNING_PASSWORD`

### Google Play Service Account

1. **Create Service Account:**
   - Go to Google Play Console → API access
   - Create new service account
   - Grant release permissions

2. **Download JSON Key:**
   - Save the JSON file
   - Copy contents to `PLAY_SERVICE_ACCOUNT_JSON` secret

### Firebase Configuration

1. **Download Google Services JSON:**
   - Go to Firebase Project Settings
   - Add Android app (if not already added)
   - Download `google-services.json`

2. **Add to GitHub Secrets:**
   - Copy file contents to `GOOGLE_SERVICES_JSON` secret

## 🚀 Deployment Workflow

### Standard Release Process

1. **Development Phase**
   - Code changes are pushed to feature branches
   - Pull requests trigger CI checks
   - All tests must pass

2. **Merge to Main**
   - PR is merged to `main` branch
   - Full CI pipeline runs automatically
   - Build artifacts are generated

3. **Internal Deployment**
   - Go to Actions → Deploy to Internal Track
   - Click "Run workflow"
   - Configure version and release notes
   - Wait for deployment completion

4. **Beta Promotion**
   - After Internal testing
   - Go to Actions → Promote to Beta Track
   - Click "Run workflow"
   - Configure promotion options
   - Wait for promotion completion

5. **Production Release**
   - After Beta testing
   - Go to Actions → Promote to Production Track
   - Click "Run workflow"
   - Configure final release options
   - Monitor deployment success

### Emergency Release

For critical bug fixes:
1. Use "Skip tests" option in Internal deployment
2. Follow standard promotion workflow
3. Monitor deployment closely

### Rollback Process

If issues are detected in production:
1. Go to Actions → Promote to Production Track
2. Enable "Rollback to previous version" option
3. Execute workflow
4. Verify rollback completion

## 📊 Monitoring and Notifications

### Deployment Status
- GitHub commit status checks show deployment progress
- Each track has its own status context
- Failed deployments create failure status

### Notifications
- **Production:** Slack, Email, GitHub Release
- **Beta:** GitHub Release
- **Internal:** Workflow status only

### Artifacts
- **Debug APK:** 30 days retention
- **Release Bundle:** 90 days retention
- **Test Reports:** 30 days retention
- **Security Reports:** 30 days retention

## 🔒 Security Considerations

### Secret Management
- All secrets are encrypted by GitHub
- Keystore is base64 encoded for safe storage
- Temporary files are cleaned up after each job

### Access Control
- Production deployments require manual approval
- Workflow dispatch respects repository permissions
- Branch protection can require additional approvals

### Security Scanning
- Trivy scans for known vulnerabilities
- OWASP dependency checks
- Automated security issue creation

## 🛠️ Troubleshooting

### Common Issues

**Build Failures:**
- Check Gradle cache in Actions logs
- Verify Java version compatibility
- Ensure all dependencies are available

**Signing Issues:**
- Verify keystore is properly encoded
- Check alias and password correctness
- Ensure keystore file is valid

**Play Store Failures:**
- Verify service account permissions
- Check API access configuration
- Ensure app is properly configured

**Permission Issues:**
- Check workflow permissions in repository settings
- Verify GitHub token scopes
- Ensure secret access is configured

### Debug Mode

For debugging, you can:
1. Add `echo` statements to workflows
2. Use `set -x` for shell debugging
3. Check individual job logs
4. Download artifacts for local inspection

## 📈 Best Practices

1. **Always test on Internal track first**
2. **Use meaningful release notes**
3. **Monitor deployments in real-time**
4. **Keep secrets secure and updated**
5. **Regular security audits of workflows**
6. **Maintain up-to-date dependencies**

## 🔄 Workflow Maintenance

- Review and update dependencies quarterly
- Monitor workflow performance and usage
- Update Java version as needed
- Rotate signing secrets annually
- Test rollback procedures periodically

## 📞 Support

For issues with these workflows:
1. Check workflow logs for error details
2. Verify all secrets are correctly configured
3. Ensure proper permissions are set
4. Review this documentation for troubleshooting steps

For workflow improvements or issues:
- Create an issue in the repository
- Tag with `ci/cd` label
- Provide detailed error logs and reproduction steps
