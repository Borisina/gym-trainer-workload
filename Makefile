include .env

## Delete gym-trainer-workload app
gym-trainer-workload-rm:
	docker rm -f gym-trainer-workload-container
	docker rmi gym-trainer-workload-image

## Build and run gym-trainer-workload app
gym-trainer-workload-up:
	docker build -t gym-trainer-workload-image .
	docker run -dp 8081:8081\
    	--network gym_gym-network --network-alias gym-trainer-workload\
        --env-file .env\
        --name gym-trainer-workload-container\
        --mount type=volume,source=logsvolume,target=/app/logs\
         gym-trainer-workload-image

## Delete gym-trainer-workload-itest app
gym-trainer-workload-itest-rm:
	docker rm -f gym-trainer-workload-itest-container
	docker rmi gym-trainer-workload-itest-image

## Build and run gym-trainer-workload-itest app
gym-trainer-workload-itest-up:
	docker build -t gym-trainer-workload-itest-image -f dockerfile-itest .
	docker run -dp 8081:8081\
		--network gym_gym-network-itest --network-alias gym-trainer-workload-itest\
        --env-file .env\
        --name gym-trainer-workload-itest-container\
         gym-trainer-workload-itest-image

mongo-sh:
	docker compose exec mongodb mongosh -u  ${MONGO_USER} -p  ${MONGO_PASSWORD}