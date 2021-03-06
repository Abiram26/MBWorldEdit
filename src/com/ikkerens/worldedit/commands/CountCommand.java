package com.ikkerens.worldedit.commands;

import com.ikkerens.worldedit.WorldEditPlugin;
import com.ikkerens.worldedit.exceptions.BlockNotFoundException;
import com.ikkerens.worldedit.handlers.AbstractCommand;
import com.ikkerens.worldedit.model.Selection;
import com.ikkerens.worldedit.model.pattern.MatchBlockType;

import com.mbserver.api.game.Location;
import com.mbserver.api.game.Player;
import com.mbserver.api.game.World;

public class CountCommand extends AbstractCommand< WorldEditPlugin > {

    public CountCommand( final WorldEditPlugin plugin ) {
        super( plugin );
    }

    @Override
    protected void execute( final String label, final Player player, final String[] args ) {
        if ( args.length != 1 ) {
            player.sendMessage( "Usage: /" + label + " <block>" );
            return;
        }

        final Selection sel = this.getSession( player ).getSelection();
        if ( sel.isValid() ) {
            final Location lowest = sel.getMinimumPosition();
            final Location highest = sel.getMaximumPosition();
            final World world = lowest.getWorld();

            MatchBlockType match;
            try {
                match = new MatchBlockType( args[ 0 ] );
            } catch ( final BlockNotFoundException e ) {
                player.sendMessage( e.getMessage() );
                return;
            }

            int result = 0;

            for ( int x = lowest.getBlockX(); x <= highest.getBlockX(); x++ )
                for ( int z = lowest.getBlockZ(); z <= highest.getBlockZ(); z++ )
                    for ( int y = lowest.getBlockY(); y <= highest.getBlockY(); y++ )
                        if ( match.matches( world.getBlockID( x, y, z ) ) )
                            result++;

            player.sendMessage( String.format( "Counted blocks: %s", result ) );
        } else
            player.sendMessage( NEED_SELECTION );
    }

}
