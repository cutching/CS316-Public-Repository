# *Juptyer Notebook*
## *Implementation Guide*
Here is the steps to modify and package a model for use in AWS SageMaker using a model definition and associated code.

1. While in this directory run the command: "Jupyter Notebook" in the command line to open a locally hosted notebook
2. Open the Tar Filer.ipynb and run each block
3. Take the resulting tar file and put in an S3 bucket

See S3 folder next.

## *Notes*
SageMaker requires a very specific directory structure inside of a tar.gz file for PyTorch specific models.
The directory is as follows:

- model_folder/
  - your_model.pth
  - code/
    - requirements.txt
    - inference.py
    - (any additional file dependencies here)



##
The point of the tar filer is to package these files in such a way that SageMaker functions
