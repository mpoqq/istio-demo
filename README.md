# istio-demo
This is a simple [istio](https://istio.io/) demo/example.
This example is based on spring boot and create automatically a docker image. 
Change the docker image prefix in the [pom.xml](./pom.xml) to be able to publish the image on your docker registry.

You will need for this demo:
* maven
* docker
* minikube
* kubectl
* istio

Example usage:

1. build both services `mvn clean install`
2. push to docker registry `docker push prefix/helloworld-service && docker push prefix/helloworld-service-caller`
3. deploy service `kubectl apply -f kubernetes/helloworld-deployment-mpo.yaml`
4. create service `kubectl apply -f kubernetes/helloworld-service-mpo.yaml`
5. deploy service caller `kubectl apply -f kubernetes/helloworld-caller-deployment-mpo.yaml`
6. create service caller `kubectl apply -f kubernetes/helloworld-caller-service-mpo.yaml`
7. install istio : `kubectl apply -f $istio-path/install/kubernetes/helm/istio/templates/crds.yaml` 
8. sidecar injection: `kubectl label namespace default istio-injection=enabled --overwrite`
9. restart services: `kubectl scale deployment helloworld-service --replicas=0 && kubectl scale deployment helloworld-service --replicas=2`
10. restart services caller: `kubectl scale deployment helloworld-service-caller --replicas=0 && kubectl scale deployment helloworld-service-caller --replicas=2`
11. create gateway: `kubectl apply -f istio-rules/helloworld-service-gateway.yml`
12. test it `curl [](http://192.168.39.79:31380/hello/world)`
13. activate the other example istio rueles