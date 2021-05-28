#!/usr/bin/env bash
# Decrypt secret files that are checked in to Git using a passphrase provided as
# an environment variable. Can be used locally or from Continuous Deployment.

# If any one command fails, immediately exit.
# Treat unset variables as an error, and immediately exit.
# Write each command to stdout before executing.
set -eux

# Keep this in sync with .gitignore to prevent the unencrypted files from being
# accidentally checked in.
ENCRYPTED_FILES=("signing-keys.keystore" "service-account-keys.json")

for FILE in ${ENCRYPTED_FILES[@]}; do
  gpg --quiet --batch --yes --decrypt \
      --passphrase="$GPG_PASSPHRASE" \
      --output "$FILE" \
      "$FILE.gpg"
done
