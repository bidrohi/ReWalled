## 📋 Pull Request Checklist

### General Requirements
- [ ] Code follows project conventions (see `AGENTS.md`)
- [ ] Tests pass locally: `./gradlew allTests`
- [ ] Code quality checks pass: `./gradlew check`
- [ ] Documentation updated if needed

### Testing Requirements
- [ ] Unit tests written for new functionality
- [ ] Integration tests updated if applicable
- [ ] Manual testing completed for UI changes
- [ ] Firebase/Play Store integration tested (if applicable)

### Build Requirements
- [ ] Debug APK builds successfully
- [ ] Release bundle builds successfully
- [ ] No new dependency vulnerabilities
- [ ] Gradle builds without warnings

### Deployment Impact
- [ ] Database migrations considered (if applicable)
- [ ] Breaking changes documented
- [ ] Rollback plan considered
- [ ] Production readiness confirmed

## 📝 Description

### Type of Change
- [ ] Bug fix (non-breaking change which fixes an issue)
- [ ] New feature (non-breaking change which adds functionality)
- [ ] Breaking change (fix or feature that would cause existing functionality to not work as expected)
- [ ] Documentation update only
- [ ] Refactoring (no functional changes)

### What does this PR do?
<!-- Describe the changes in detail -->

### Why is this change needed?
<!-- Explain the motivation for the change -->

### How was this implemented?
<!-- Brief technical explanation -->

### How to test this change?
<!-- Provide testing instructions -->

### Screenshots (if applicable)
<!-- Add screenshots to help explain your changes -->

## 🔗 Related Issues

Closes: #<!-- issue number -->
Related to: #<!-- issue number -->

## 📋 Additional Notes

<!-- Any additional context or notes -->

## 🚨 Deployment Notes

<!-- Any special considerations for deployment -->
- [ ] Requires database migration
- [ ] Requires Play Store configuration changes
- [ ] Requires Firebase configuration changes
- [ ] Safe for automatic deployment
