package com.bigprime.source.spi.interfaces;

import com.bigprime.source.spi.model.CommandModel;

/**
 * @author lyw
 * @version 1.0
 */
public interface ISourceCommand {

    default CommandModel host(){
        return CommandModel.builder()
                .required(true)
                .build();
    }

    default CommandModel port(){
        return CommandModel.builder()
                .required(true)
                .build();
    }

    default CommandModel username(){
        return CommandModel.builder()
                .show(true)
                .build();
    }

    default CommandModel password(){
        return CommandModel.builder()
                .show(true)
                .build();
    }

    default CommandModel schema(){
        return CommandModel.builder()
                .show(false)
                .build();
    }

    default CommandModel database(){
        return CommandModel.builder()
                .show(true)
                .build();
    }

    default CommandModel ssl(){
        return CommandModel.builder()
                .show(false)
                .build();
    }

    default CommandModel configures(){
        return CommandModel.builder().build();
    }
}
