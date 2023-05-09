# *AWS Implementation*
## *File Structure*
- AWS/
  - Lambda/
    - lambda_function.py
    - README.md
  - NoteBook/
    - code/
      - bethInference.py
      - dataset.py
      - requirements.txt
      - vae.py
    - beth_dose_1.pth
    - Modelfn.ipynb
    - README.md
    - Tar Filer.ipynb
  - S3/
    - my_model.tar.gz
    - README.md
  - SageMaker/
    - Beth Training and Validation Testing.ipynb
    - README.md





##
The directory structure in this GitHub is to allow for recreation on another instance of AWS. 
Each folder contains a README file to guide a user through personal implementation for model hosting and inference using a trained model, in this case we use the BETH model from another project in our directory.
##
The recommended order of setup would be:
1. NoteBook
2. S3
3. SageMaker
4. Lambda
