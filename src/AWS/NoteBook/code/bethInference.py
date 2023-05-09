from dataset import DATASETS
from vae import *
from math import *
import torch
from torch.utils.data import DataLoader
from torch.distributions import MultivariateNormal
import json
import os


def model_fn(model_dir):
    """
    Load the model for inference
    """
    dataset = DATASETS["beth"](csv_string="s3://sagemaker-us-east-1-510417191883/model/pytorch/code/labelled_training_data.csv", subsample=0) 
    input_shape = dataset.get_input_shape()
    model = VAE(input_shape=input_shape, latent_size=2, hidden_size=64, observation=dataset.get_distribution())
    #model.load_state_dict(torch.load(model_dir, map_location=torch.device('cpu')))
    with open(os.path.join(model_dir, "beth_dose_1.pth"), 'rb') as f:
       model.load_state_dict(torch.load(f, map_location=torch.device('cpu')))
    model.to('cpu').eval()
    return model

def input_fn(request_body, request_content_type):
    """
    Deserialize and prepare the prediction input
    """
    #assert request_content_type=='application/json'
    data = json.loads(request_body)
    input_dataset = DATASETS["beth"](csv_string=str(data), subsample=0)
    return input_dataset

def predict_fn(input_object, model):
    """
    Apply model to the incoming request
    """
    prior = MultivariateNormal(torch.zeros(2, device='cpu'), torch.eye(2, device='cpu'))
    data_loader = DataLoader(input_object, batch_size=128, num_workers=4, pin_memory=True)
    with torch.no_grad():
        for i, (x, y) in enumerate(data_loader):
            x, y = x.to(device='cpu', non_blocking=True), y.to(device='cpu', non_blocking=True)
            observation, posterior, z = model(x)
    return kl_divergence(z, posterior, prior), posterior.log_prob(z), prior.log_prob(z)


def output_fn(prediction, content_type):
    """
    Serialize and prepare the prediction output
    """
    #assert content_type=='application/json'
    one, two, three = prediction
    result = abs(one.tolist()[0])
    result = "{:.00%}".format(result)
    two = two.tolist()[0]
    three = three.tolist()[0]
    result = result + " , " + str(two) + " , " + str(three)
    return json.dumps(result)

def kl_divergence(z, P, Q):
    return P.log_prob(z) - Q.log_prob(z)
