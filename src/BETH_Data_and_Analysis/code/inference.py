from dataset import DATASETS
from vae import *

def model_fn(model_dir):
    """
    Load the model for inference
    """
    #val and test functionality
    dataset = DATASETS["beth"](split=model_dir, subsample=0) 
    input_shape = dataset.get_input_shape()
    model = VAE(input_shape=input_shape, latent_size=2, hidden_size=64, observation=dataset.get_distribution())
    model.to(torch.device("cuda:0" if torch.cuda.is_available() else "cpu"))
    model = torch.jit.load('beth_dose_1.pth', map_location=torch.device('cpu')) 
    
    with open(model_dir, 'wb') as file:  
        torch.save(model, file)
    
    return model

def input_fn(input_csv_path, request_content_type):
    """
    Deserialize and prepare the prediction input
    """
    input_dataset = DATASETS["beth"](csv_path=input_csv_path, subsample=0)
    
    return input_dataset

def predict_fn(input_object, model):
    """
    Apply model to the incoming request
    """
    return model.__call__(input_object)

def output_fn(prediction, response_content_type):
    """
    Serialize and prepare the prediction output
    """
    return str(prediction)