# *AWS SageMaker*
## *Implementation Guide*
Here is the steps to begin hosting a model from model artifacts within your S3 bucket:

1. All that is needed here is to create a new Notebook instance from under the SageMaker Notebook tab and name it whatever you'd like, all default values can remain the same for this notebook, just make sure the IAM Role has full S3, Sagemaker, and Lambda access for the purposes of the model.
2. Once created upload the "Beth Training and Validation Testing.ipynb" file to the notebook.
3. Run only the first block in the file to create and host a model in SageMaker. If the S3 urls differ than what is provided then you will have to modify them to your own implementation.
4. Model takes about 5-10 minutes to start up, once the model is up and verified to be working on the dashboard, lambda functions can now be used on the model.

See Lambda folder next.