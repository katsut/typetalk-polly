#!/bin/sh
# ==============================================
# build aws serverless framework application
# $1 stage {lcl, dev, stg, prd}
# ==============================================
echo "===== build aws serverless framework application ====="
readonly STAGE=${1:-"lcl"}
echo "STAGE:${STAGE}"


sbt assembly

if [ ! "$?" -eq 0 ]; then
  # fail
  echo "FAILED: serverless deploy -s ${STAGE}"
  exit -1
fi

sls deploy -v -s dev

