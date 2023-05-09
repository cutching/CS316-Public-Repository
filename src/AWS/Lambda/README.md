# *AWS Lambda*
## *Implementation Guide*
In the AWS Lambda console, create a new lambda function and follow these steps.

1. Author from scratch
2. Function name can be anything you want
3. Runtime --> Python 3.8
4. Architecture x86_64

Once completed you can copy and paste the lamda_function.py code into the function and save. 
##
In this code we used environment variables under the configurations tab. Add a variable names ENDPOINT_NAME and the value is whatever you named your endpoint to run inference on.
##
Once this is done you have the option to set up API Gateway access through AWS to this Lambda function to send data to your model and store it.