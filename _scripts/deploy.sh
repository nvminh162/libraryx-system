cd /app/libraryx-system/

BRANCH=$(git rev-parse --abbrev-ref HEAD)

echo "Deploying with branch: $BRANCH"

git checkout $BRANCH
git fetch -a
git pull

docker compose -f docker-compose.yml down
docker compose -f docker-compose.yml pull discover-server
docker compose -f docker-compose.yml up -d discover-server
# clean up unused images and containers
docker system prune -af
