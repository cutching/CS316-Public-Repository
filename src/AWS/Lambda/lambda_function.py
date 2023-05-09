import os 
import io 
import boto3 
import json 
import csv 


# grab environment variables 
ENDPOINT_NAME = os.environ['ENDPOINT_NAME'] 
runtime= boto3.client('sagemaker-runtime')
def lambda_handler(event, context): 
    print("Received event: " + json.dumps(event, indent=2)) 
    data = json.loads(json.dumps(event)) 
    payload = data['data']
    print(payload + "whaty")
    response = runtime.invoke_endpoint(
        EndpointName=ENDPOINT_NAME, 
        ContentType="application/json",
        Accept="application/json",
        Body=json.dumps(payload)
    )
    print(response) 
    result = json.loads(response['Body'].read().decode())
    print(result) 
    return result