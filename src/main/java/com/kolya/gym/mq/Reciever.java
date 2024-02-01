package com.kolya.gym.mq;

import com.kolya.gym.data.TrainerWorkloadRequestData;
import com.kolya.gym.service.TrainerWorkloadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import javax.jms.Message;
import java.util.UUID;

@Service
public class Reciever {
    private final Logger logger = LoggerFactory.getLogger(Reciever.class);
    @Autowired
    private TrainerWorkloadService trainerWorkloadService;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Value("${mq.queue.name.add}")
    private String QUEUE_NAME_ADD;

    @Value("${mq.queue.name.delete}")
    private String QUEUE_NAME_DELETE;
    @Value("${mq.queue.name.dlq}")
    private String QUEUE_NAME_DLQ;

    @JmsListener(destination = "${mq.queue.name.add}")
    public void recieveAdd(TrainerWorkloadRequestData trainerWorkloadData){
        UUID transactionId = UUID.randomUUID();
        logger.info("Transaction ID: {}, Message recieved. Queue: {}, Message: {}", transactionId, QUEUE_NAME_ADD, trainerWorkloadData);
        try{
            trainerWorkloadService.validateRequestData(trainerWorkloadData);
        }catch (IllegalArgumentException e){
            jmsTemplate.convertAndSend(QUEUE_NAME_DLQ, trainerWorkloadData,
                    message ->  setPropertyForMessage(message,"dlqDeliveryFailureCause",e.getMessage()));
            logger.info("Transaction ID: {}, TrainerWorkloadRequestData is invalid. Message redirected to dlq.", transactionId);
            return ;
        }
        trainerWorkloadService.addTraining(transactionId, trainerWorkloadData);
        logger.info("Transaction ID: {}, Training added after recieving.", transactionId);
    }

    @JmsListener(destination = "${mq.queue.name.delete}")
    public void recieveDelete(TrainerWorkloadRequestData trainerWorkloadData){
        UUID transactionId = UUID.randomUUID();
        logger.info("Transaction ID: {}, Message recieved. Queue: {}, Message: {}", transactionId, QUEUE_NAME_DELETE, trainerWorkloadData);
        try{
            trainerWorkloadService.validateRequestData(trainerWorkloadData);
        }catch (IllegalArgumentException e){
            jmsTemplate.convertAndSend(QUEUE_NAME_DLQ, trainerWorkloadData,
                    message ->  setPropertyForMessage(message,"dlqDeliveryFailureCause",e.getMessage()));
            logger.info("Transaction ID: {}, TrainerWorkloadRequestData is invalid. Message redirected to dlq.", transactionId);
            return ;
        }
        trainerWorkloadService.deleteTraining(transactionId, trainerWorkloadData);
        logger.info("Transaction ID: {}, Training deleted after recieving.", transactionId);
    }

    private Message setPropertyForMessage(Message message, String propertyName, String value) throws JMSException {
        message.setStringProperty(propertyName,value);
        return message;
    }

}

